package com.clinic.dto;

import com.clinic.enums.PayWay;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 病人收费列表返回
 */
@Data
public class PayRecordPatientDto {

    private Long id;

    /**
     * 费用
     */
    private BigDecimal fee;

    /**
     * 状态：0未支付，1已支付
     */
    private String state;

    /**
     * 收费方式：1微信，2支付宝，3挂账，4现金
     */
    private PayWay way;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;
    /**
     * 就诊时间，既创建病例时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date dossierTime;

    /**
     * 初步诊断
     */
    private String diagnosis;

}
