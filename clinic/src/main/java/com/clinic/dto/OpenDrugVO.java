package com.clinic.dto;

import com.clinic.entity.PrescriptionDrug;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 药房开药
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenDrugVO {

    /**
     * 患者姓名
     */
    private String name;

    /**
     * 处方药列表
     */
    private List<PrescriptionDrug> drugs;

    /**
     * 性别状态: 1.男;0.女;
     */
    private Integer sexStatus;

    /**
     * 手机号
     */
    private Long phone;

    /**
     * 费用
     */
    private BigDecimal fee;

    /**
     * 接诊日志id
     */
    private Long admissId;

    /**
     * 支付id
     */
    private Long payId;

    /**
     * 收费状态: 0.未支付;1.已支付;
     */
    private Integer payStatus;

    /**
     * 处方时间
     */
    private Long time;
}