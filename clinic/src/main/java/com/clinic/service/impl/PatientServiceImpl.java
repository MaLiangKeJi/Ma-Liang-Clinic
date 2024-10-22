package com.clinic.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.api.auth.User;
import com.bbs.util.FirstWordsSqlUtils;
import com.bbs.util.MyStringUtil;
import com.bbs.util.PageUtil;
import com.clinic.cache.log.admission.AdmissionLogCache;
import com.clinic.converter.PatientConverter;
import com.clinic.dao.PatientDao;
import com.clinic.dto.AddOrEditPatientVo;
import com.clinic.dto.param.AddPatientParam;
import com.clinic.dto.param.EditPatientParam;
import com.clinic.dto.param.PatientParam;
import com.clinic.entity.Dossier;
import com.clinic.entity.Patient;
import com.clinic.mapper.PatientMapper;
import com.clinic.service.PatientService;
import com.clinic.util.LoginUser;
import com.clinic.util.log.LogUtil;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.hutool.core.lang.Validator.isWord;
import static com.bbs.Result.failed;
import static com.bbs.Result.success;
import static java.util.Objects.nonNull;

/**
* @author 29080
* @description 针对表【patient(病人信息登记表)】的数据库操作Service实现
* @createDate 2023-05-24 17:28:35
*/
@Slf4j
@Service
public class PatientServiceImpl extends MPJBaseServiceImpl<PatientMapper, Patient> implements PatientService{

    @Resource
    private PatientDao dao;

    @Resource
    private AdmissionLogCache admissionLogCache;

    @Resource
    private PatientConverter converter;

    @Resource
    private DataSourceTransactionManager transactionManager;

    @Resource
    private TransactionDefinition transactionDefinition;

    @Override
    public Result<AddOrEditPatientVo> add(AddPatientParam param) {
        User user = LoginUser.get();
        Patient patient = converter.toEntity(param);
        if(patientExists(patient)) return failed(400, "病人信息已存在！");
        patient.setUserId(user.getId());
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        try {
            save(patient);
            Long logId = admissionLogCache.saveLogFormAddPatient(patient);
            LogUtil.Operation.addPatient(patient.getId(), "{}添加病人：病人id={}, 门诊日志id={}", user.getName(), patient.getId(), logId);
            transactionManager.commit(transaction);
            return success(new AddOrEditPatientVo(patient.getId(), logId));
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Result<Long> edit(EditPatientParam param) {
        Patient patient = converter.toEntity(param);
        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        try {
            updateById(patient);
            LogUtil.Operation.updatePatient(patient.getId(), "{}修改病人：{}", LoginUser.get().getName(), patient.getName());
            return success(patient.getId());
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private Boolean patientExists(Patient patient) {
        LambdaQueryChainWrapper<Patient> wrapper = lambdaQuery()
                .eq(nonNull(patient.getName()), Patient::getName, patient.getName())
                .eq(nonNull(patient.getSex()), Patient::getSex, patient.getSex())
                .likeRight(nonNull(patient.getPhone()), Patient::getPhone, patient.getPhone());
        return wrapper.exists();
    }

    @Override
    public Result<Page<Patient>> select(PatientParam param) {
        String name = param.getName();
        MPJLambdaWrapper<Patient> wrapper = new MPJLambdaWrapper<Patient>()
                .selectAll(Patient.class)
                .leftJoin(Dossier.class, Dossier::getPatientId, Patient::getId)
                .selectCount(Dossier::getId, Patient::getDossierNum);
        if(StringUtils.isNotBlank(name)){
            if(isWord(name)){
                String sql = FirstWordsSqlUtils.getSql(name);
                wrapper = wrapper.apply(StringUtils.isNotEmpty(sql), sql);
            } else{
                wrapper = wrapper.like(StringUtils.isNoneBlank(name), Patient::getName, name);
            }
            wrapper = wrapper.like(NumberUtil.isNumber(name), Patient::getPhone, name);
        }
        wrapper = wrapper.eq(Patient::getUserId, LoginUser.getId())
                .groupBy(Patient::getId);
        List<Patient> list = selectJoinList(Patient.class, wrapper);
        Page<Patient> page = PageUtil.paginateWithInfo(list, param.getCurrent(), param.getSize());

        updateAgeAndIsFirstToDB(page.getRecords());
        return success(page);
    }

    @Override
    public List<Patient> select(String val) {
        return lambdaQuery()
                .eq(Patient::getUserId, LoginUser.getId())
                .like(Patient::getName, val)
                .or()
                .like(Patient::getPhone, val)
                .or()
                .like(Patient::getAddress, val)
                .list();
    }

    @Override
    public List<Patient> selectListByPhone(String phone) {
        return lambdaQuery()
                .eq(Patient::getUserId, LoginUser.getId())
                .likeRight(Patient::getPhone, phone)
                .list();
    }

    @Override
    public Patient selectByPhone(String phone) {
        return lambdaQuery()
                .eq(Patient::getUserId, LoginUser.getId())
                .eq(Patient::getPhone, phone)
                .one();
    }

    @Override
    public List<Patient> selectByName(String name) {
        return selectByName(name, null);
    }

    @Override
    public List<Patient> selectByName(String name, Long userId) {
        return lambdaQuery()
                .eq(Patient::getUserId, nonNull(userId) ? userId :LoginUser.getId())
                .like(Patient::getName, name)
                .list();
    }

    private void updateAgeAndIsFirstToDB(List<Patient> patientList){
        if(!patientList.isEmpty()) {
            patientList.forEach(patient->{
                updateAge(patient);
                updateIsFirstAndDB(patient);
            });
            dao.asyncUpdate(filterOldAgeAndAttendPatient(patientList));
        }
    }

    private boolean updateIsFirstAndDB(Patient patient) {
        if(patient.getDossierNum()>=1){
            patient.setIsFirst(1);
            return true;
        }
        return false;
    }


    /**
     * 筛选需要更新年龄的病人信息
     * @param patientList 病人信息
     * @return 需要更新年龄的病人信息
     */
    private List<Patient> filterOldAgeAndAttendPatient(List<Patient> patientList) {
        return patientList.stream().filter(o->updateAge(o)||updateIsFirstAndDB(o)).collect(Collectors.toList());
    }

    /**
     * 获取最新年龄
     * @param updateTime  修改时间
     * @param age 年龄
     * @return
     * 1.获取当前年份，年龄和修改时间 2.计算出相差年份 3.计算最新年龄
     */
    private static Integer updateAge(Date updateTime, int age){
        Date date = DateUtil.date();
        int difYear = (DateUtil.year(date)) - (DateUtil.year(updateTime));
        age = age + difYear;
        return age;
    }

    private static Boolean updateAge(Patient patient){
        Integer oldAge = patient.getAge();
        if(Objects.nonNull(oldAge)) {
            Integer newAge = updateAge(patient.getUpdateTime(), patient.getAge());
            if(!newAge.equals(oldAge)) {
                patient.setAge(newAge);
                return true;
            }
        }
        return false;
    }
}




