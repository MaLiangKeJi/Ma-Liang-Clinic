package com.clinic.app.reception.over;

import cn.hutool.core.util.StrUtil;
import com.clinic.dto.param.SaveOrUpdatePrescriptionDrug;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 处方
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionParam {

    /**
     * 处方药品
     */
    @Valid
    @NotNull
    private List<SaveOrUpdatePrescriptionDrug> drugList;

    /**
     * 总价，药方药品总价之和
     */
    @NotNull(message = "总价不能为空！")
    private BigDecimal price;

    /**
     * 备注
     */
    private String remark;

    public @Valid @NotNull List<SaveOrUpdatePrescriptionDrug> getDrugList() {
        //清除空对象
        return drugList.stream().filter(drug -> StrUtil.isNotEmpty(drug.getName())).collect(Collectors.toList());
    }
}