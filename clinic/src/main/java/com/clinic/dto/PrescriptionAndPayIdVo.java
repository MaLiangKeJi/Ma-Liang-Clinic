package com.clinic.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PrescriptionAndPayIdVo {

    private Long prescriptionId;

    private Long payId;

    public PrescriptionAndPayIdVo(Long prescriptionId, Long payId) {
        this.prescriptionId = prescriptionId;
        this.payId = payId;
    }

}
