package com.clinic.dto.param;

import com.bbs.vo.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitParam extends BaseParam {
    /**
     * 编号
     */
    private Integer id;

    /**
     * 展示内容
     */
    private String name;

    /**
     * 父级编号
     */
    private Long parentId;

    /**
     * 是否为默认模板（0 非默认，1 默认）
     */
    private Integer defaultState;
}
