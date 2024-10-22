package com.clinic.dto.param;

import com.clinic.entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecordAdmissionLogParam  {

    /**
     * 病人ID
     */
    private Long patientId;

    /**
     * 初复诊（0初诊/1复诊）
     */
    private Integer isFirst;


    /**
     * 病人信息（如果病人 ID 未传，则代表需要新增病人，再保存日志）
     */
    private Patient patient;

    /**
     * 微信 openId
     */
    private String openId;

}
