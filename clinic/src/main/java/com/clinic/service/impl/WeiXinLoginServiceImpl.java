package com.clinic.service.impl;


import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.bbs.exception.BusinessException;
import com.clinic.entity.AdmissionLog;
import com.clinic.entity.Patient;
import com.clinic.entity.PrescriptionDrug;
import com.clinic.service.AdmissionLogService;
import com.clinic.service.PatientService;
import com.clinic.service.PrescriptionDrugService;
import com.clinic.service.WeiXinLoginService;
import com.clinic.util.RedisUtil;
import com.clinic.util.WxUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

@Service
@Slf4j
public class WeiXinLoginServiceImpl implements WeiXinLoginService {

    @Value("${wx.oa.token}")
    private String token;

    @Value("${wx.drug.tempId}")
    private String tempIdByDrug;//处方药模板id

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private WxUtil wxUtil;

    @Resource
    private PatientService patientService;
    @Resource
    private AdmissionLogService admissionLogService;

    @Resource
    private PrescriptionDrugService drugService;

    /**
     * 验证当前手机号的病人是否扫码关注
     * @param ticket
     * @param phone
     * @return
     */
    @Override
    public Map<String, Object> checkPhone(String website, String ticket, Long phone, boolean isEnd) {
        // 从缓存获取扫码状态
        String openId;
        try {
            openId = redisUtil.get("WX:"+ticket);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.debug("redis中的openid：{}，1表达没有扫码或没有回调或没有关注关注号",openId);
        // 判断扫码状态
        if (StrUtil.isEmpty(ticket)){
            throw new BusinessException(phone+"WX:"+ticket+"的值为空");
        }
        if (openId == null){
            //说明二维码过期了，停止轮询
            HashMap<String, Object> scanResultMap2 = new HashMap<>();
            scanResultMap2.put("scanResult",-2);
            return scanResultMap2;
        }
        if(openId.equals("1")){
            //1表达没有回调，没有关注关注号
            HashMap<String, Object> scanResultMap = new HashMap<>();
            scanResultMap.put("scanResult",-1);
            return scanResultMap;
        }
        HashMap<String, Object> scanResultMap3 = new HashMap<>();
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, token.getBytes());
        scanResultMap3.put("openId", aes.encrypt(openId));
        scanResultMap3.put("scanResult",1);

        Patient patient;
        if(isEnd){
            AdmissionLog dbOne = getLast(phone);
            patient = patientService.getById(dbOne.getPatientId());
            dbOne.setOpenId(openId);
            patient.setOpenId(openId);
            patientService.updateById(patient);
            admissionLogService.lambdaUpdate().set(AdmissionLog::getOpenId, openId).eq(AdmissionLog::getId, dbOne.getId()).update();
            //发送绑定成功消息
            wxUtil.sendPatientMassage(patient);
            //发送当天最近一次就诊处方记录
            List<PrescriptionDrug> drugList = getDrugList(dbOne.getPrescriptionId());
            sendWxMsgByDrug(website, dbOne.getPrescriptionId(),openId, drugList);
        }else{
            patient = patientService.selectByPhone(String.valueOf(phone));
            if(Objects.isNull(patient)){
                //病人还未创建完，暂时存到redis
                redisUtil.set("PATIENT:"+phone, openId);
            }else{
                patient.setOpenId(openId);
                patientService.updateById(patient);
                //发送绑定成功消息
                wxUtil.sendPatientMassage(patient);
            }
        }
        return scanResultMap3;
    }

    private AdmissionLog getLast(Long phone) {
        return admissionLogService.selectJoinList(AdmissionLog.class,
                        new MPJLambdaWrapper<AdmissionLog>()
                                .eq(AdmissionLog::getPhone, phone)
                                .orderByDesc(AdmissionLog::getCreateTime))
                .get(NumberUtils.INTEGER_ZERO);
    }

    /**
     * 获取处方药列表
     *
     * @param presId 处方id
     */
    private List<PrescriptionDrug> getDrugList(Long presId) {
        List<PrescriptionDrug> resultList= drugService.selectJoinList(PrescriptionDrug.class,
                new MPJLambdaWrapper<PrescriptionDrug>()
                        .eq(PrescriptionDrug::getPrescriptionId, presId));

        if (ObjectUtils.isEmpty(resultList))
            resultList= Collections.emptyList();

        return resultList;
    }

    /**
     * 发送处方消息
     *
     * @param website  官网网址
     * @param presId   处方id
     * @param drugList 处方药列表
     */
    private void sendWxMsgByDrug(String website, Long presId, String openId, List<PrescriptionDrug> drugList) {
        if (drugList.isEmpty())
            return;

        Map<String, Object> extParams = new HashMap<>();
        boolean isMore = drugList.size() > NumberUtils.INTEGER_ONE;//多副处方药
        if (isMore)
            extParams.put("url", String.format("%s/clinic/drug?presId=%d", website, presId));

        PrescriptionDrug drugByOne = drugList.get(NumberUtils.INTEGER_ZERO);
        wxUtil.sendTempMsg(openId, tempIdByDrug, WxUtil.getDataMap(getDrugMapByOne(drugByOne, !isMore)), extParams);
    }

    /**
     * 获取第一个处方药的模板消息映射
     *
     * @param drug  第一个处方药
     * @param isOne true: 一副药; false: 多副药;
     */
    private Map<String, Object> getDrugMapByOne(PrescriptionDrug drug, boolean isOne) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("thing3", drug.getName());
        resultMap.put("thing8", String.format("%s | %s", drug.getDrugUsage(), drug.getFrequency()));
        resultMap.put("thing4", String.format("单次剂量: %s%s | 天数: %s%s", drug.getSingleDose(), drug.getSingleDoseUnit(), drug.getPeriod(), drug.getPeriodUnit()));

        if (isOne)
            resultMap.put("thing7", "祝您早日康复");
        else
            resultMap.put("thing7", "点击我查看更多处方药的服用指南");

        return resultMap;
    }
}