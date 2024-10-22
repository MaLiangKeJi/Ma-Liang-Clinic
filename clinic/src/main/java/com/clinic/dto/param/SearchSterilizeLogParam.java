package com.clinic.dto.param;


import com.bbs.vo.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SearchSterilizeLogParam extends BaseParam {

    /**
     * 编号
     */
    private Long id;

    /**
     * 消毒人
     */
    private String executor;

    /**
     * 消毒时间
     */
    private String createTime;

    /**
     * 消毒时间
     */
    private Date startSterilizeTime;

    /**
     * 消毒时间
     */
    private Date endSterilizeTime;
}
