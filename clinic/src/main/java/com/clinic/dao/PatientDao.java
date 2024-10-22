package com.clinic.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.util.FirstWordsSqlUtils;
import com.clinic.entity.Dossier;
import com.clinic.entity.Patient;
import com.clinic.mapper.PatientMapper;
import com.clinic.util.LoginUser;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

import static cn.hutool.core.lang.Validator.isWord;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


@Component
public class PatientDao extends ServiceImpl<PatientMapper, Patient> {

    /**
     * 病人是否存在
     * @param patient 查找参数
     * @return 病人是否存在
     */
    public boolean patientIsExists(Patient patient){
        return baseMapper.exists(query(patient));
    }

    private MPJLambdaWrapper<Patient> query(Patient param) {
        String name = param.getName();
        Integer age = param.getAge();
        Integer sex = param.getSex();
        Long phone = param.getPhone();
        String lastVisit = param.getLastVisit();
        String remark = param.getRemark();
        String address = param.getAddress();
        Integer marriage = param.getMarriage();
        MPJLambdaWrapper<Patient> wrapper = JoinWrappers.lambda(Patient.class)
                .select(Patient::getId)
                .selectCount(Dossier::getId,Patient::getDossierNum)
                .leftJoin(Dossier.class,Dossier::getPatientId,Patient::getId)
                .eq(Patient::getUserId, LoginUser.getId())
                .eq(nonNull(sex), Patient::getSex, sex)
                .eq(nonNull(age) && age >= 0 && age <= 120, Patient::getAge, age)
                .likeRight(nonNull(phone) && phone.toString().length() <= 11, Patient::getPhone, phone)
                .eq(nonNull(marriage) && (marriage == 1 || marriage == 2), Patient::getMarriage, marriage)
                .eq(isNotBlank(lastVisit), Patient::getLastVisit, lastVisit)
                .eq(isNotBlank(remark), Patient::getRemark, remark)
                .like(nonNull(address) && address.length() <= 500, Patient::getAddress, address);
        if(StringUtils.isNotBlank(name)){
            if(isWord(name)){
                String sql = FirstWordsSqlUtils.getSql(name);
                wrapper.apply(sql);
            }else{
                wrapper.like(Patient::getName,name);
            }
        }
        return wrapper;
    }

    /**
     * 异步更新
     * @param patientList patientList
     */
    @Async("asyncMethodThreadPool")
    public void asyncUpdate(List<Patient> patientList){
        updateBatchById(patientList);
    }

    /**
     * 根据姓名查列表
     * @param name 姓名
     * @return patientList
     */
    public List<Patient> selectByName(String name) {
       return lambdaQuery().like(isNotBlank(name), Patient::getName, name).list();
    }

    /**
     * 缺省查询
     * @param param Patient
     * @param start 就诊时间：范围起始（可选）
     * @param end 就诊时间：范围结束（可选）
     */
    public Page<Patient> select(Patient param, String start, String end, Page<Patient> page) {
        return baseMapper.selectJoinPage(page,Patient.class,query(param)
                .between(
                        isBlank(param.getLastVisit()) &&
                                isNotBlank(start) &&
                                isNotBlank(end),
                        Patient::getLastVisit, start, end
                ).orderByDesc(Patient::getCreateTime).groupBy(Patient::getId));
    }
}
