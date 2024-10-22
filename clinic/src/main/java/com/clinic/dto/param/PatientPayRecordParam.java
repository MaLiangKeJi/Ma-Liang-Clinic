package com.clinic.dto.param;

import com.bbs.vo.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PatientPayRecordParam extends BaseParam {

    /**
     * 病人编号
     */
    @NotNull
    private Long patientId;

}
