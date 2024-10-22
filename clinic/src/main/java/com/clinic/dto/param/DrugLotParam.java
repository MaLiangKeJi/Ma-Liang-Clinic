package com.clinic.dto.param;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class DrugLotParam {

    /**
     * 0入库/1出库
     */
    private Integer lotType;

    /**
     * 备注
     */
    private String remark;

    /**
     *  用户ID
     */
    private Long userId;

    /**
     * 创建人
     */
    private String createName;


    /**
     * 药品集合
     */
    @Valid
    @NotNull
    private List<DrugDetailParam> drugs;



}
