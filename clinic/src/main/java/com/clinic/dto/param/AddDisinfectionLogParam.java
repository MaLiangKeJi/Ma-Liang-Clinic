package com.clinic.dto.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddDisinfectionLogParam {

    /**
     * 消杀日期
     */
    private Date disinfectionTime;

    /**
     * 消杀内容
     */
    private String content;

    /**
     * 消杀药品、器械
     */
    private String items;

    /**
     * 消杀/营业时间范围
     */
    private Date startTimeRange;

    /**
     * 消杀/营业时间范围
     */
    private Date endTimeRange;

    /**
     * 消杀地点
     */
    private String spot;

    /**
     * 消杀人
     */
    private String executor;
}
