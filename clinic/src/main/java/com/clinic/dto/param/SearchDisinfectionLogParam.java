package com.clinic.dto.param;

import com.bbs.vo.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchDisinfectionLogParam extends BaseParam {

    /**
     * 消杀时间
     */
    private String createTime;
}
