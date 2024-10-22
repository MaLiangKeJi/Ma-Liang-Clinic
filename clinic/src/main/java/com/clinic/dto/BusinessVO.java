package com.clinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 营业
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessVO {
    /**
     * 医师姓名
     */
    private String physician;

    /**
     * 当日时间戳
     */
    private Long dayTime;

    /**
     * 手动修改营业状态的标识符
     */
    private Boolean isWork;

    /**
     * 营业天数列表
     */
    private List<String> businessDayList;

    /**
     * 营业时间列表
     */
    private List<List<String>> businessTimeList;
}