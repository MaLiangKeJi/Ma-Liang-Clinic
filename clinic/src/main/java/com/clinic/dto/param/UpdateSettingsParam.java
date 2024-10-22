package com.clinic.dto.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Data
public class UpdateSettingsParam {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空")
    private Long id;

    /**
     * 过期预提醒时间（月）
     */
    private Integer expiryAlertMonth;

    /**
     * 库存统计规则
     */
    private Integer stateCountRule;

    /**
     * 统计值（统计方式值，如百分比 10%； 数量）
     */
    private Integer countVal;

    /**
     * 统计单位(0最小单位1最大单位)
     */
    private Integer countUnit;


    /**
     * 科别：内科，中西医结合，中医
     */
    private String division;

    /**
     * 诊所名称
     */
    private String clinicName;

    /**
     * 显示在处方单上的诊所名
     */
    private String alternateName;

    /**
     * 医师姓名
     */
    private String physician;

    /**
     * 详细地址
     */
    private String addr;

    /**
     * 是否由药房收费，0否1是
     */
    private Integer isPharmacyPay;

    /**
     * 营业天数
     */
    private List<Integer> businessDayList;

    /**
     * 营业时间
     */
    private List<Map<Integer, List<String>>> businessTimes;


}