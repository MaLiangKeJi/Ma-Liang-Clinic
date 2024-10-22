package com.clinic.dto.param;

import lombok.Data;

import java.util.Date;

@Data
public class StatsParam {

//    /**
//     * 统计日期类型：1日 2周 3月 4年
//     */
//    private Integer countDate;
//
//    /**
//     * 统计日期范围
//     */
//    private Integer countDateScope;

    /**
     * 开始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;



}
