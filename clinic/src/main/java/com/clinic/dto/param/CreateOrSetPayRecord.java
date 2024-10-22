package com.clinic.dto.param;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
@Data
public class CreateOrSetPayRecord {

    /**
     * 项目名称
     */
    @NotNull
    private String name;

    /**
     * 费用
     */
    @NotNull
    private BigDecimal fee;

    /**
     * 备注
     */
    private String remark;

}
