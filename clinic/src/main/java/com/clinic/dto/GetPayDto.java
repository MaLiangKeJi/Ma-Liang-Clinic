package com.clinic.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class GetPayDto {

    /**
     *
     */
    private Long id;

    /**
     * 处方id
     */
    private Long prescriptionId;

    /**
     * 费用
     */
    private BigDecimal fee;

    /**
     * 状态：0未支付，1已支付
     */
    private Integer state;

    /**
     * 收费方式：1微信，2支付宝，3挂账，4现金
     */
    private Integer way;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 就诊时间，既创建病例时间
     */
    private Date dossierTime;

    /**
     * 初步诊断
     */
    private String diagnosis;

    /**
     * 用户id
     */
    private Long creator;

    /**
     * 病人编号
     */
    private Long patientId;

    /**
     * 其他收费列表
     */
    @TableField(exist = false)
    private List<PayRecordDto> payRecordDtos;


}
