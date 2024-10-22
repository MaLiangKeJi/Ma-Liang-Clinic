package com.clinic.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 全部收费项目返回
 */
@Data
public class PayRecordDto {


    private Long id;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 费用
     */
    private BigDecimal fee;

    /**
     * 备注
     */
    private String remark;



}
