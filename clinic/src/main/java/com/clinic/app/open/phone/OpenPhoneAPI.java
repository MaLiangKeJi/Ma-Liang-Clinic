package com.clinic.app.open.phone;

import com.bbs.Result;
import com.clinic.dto.vo.PatientClinicVo;
import com.clinic.entity.AdmissionLog;
import com.clinic.service.AdmissionLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 手机端开放接口
 */
@RequestMapping
@RestController
public class OpenPhoneAPI {

    @Resource
    private AdmissionLogService admissionLogService;

    /**
     * 查询处方
     */
    @GetMapping("/open/prescription")
    public Result<AdmissionLog> searchPrescription(Long admissionId){
        return Result.success(admissionLogService.getJoinById(admissionId));
    }



    /**
     * 查历史门诊记录
     */
    @GetMapping("/open/patient/clinic")
    public Result<List<PatientClinicVo>> searchHistoryAdmission(String openId) {
        return Result.success(admissionLogService.selectByOpenId(openId));
    }

}
