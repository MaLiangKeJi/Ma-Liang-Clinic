package com.clinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddOrEditPatientVo {

    private Long patientId;

    private Long admissionLogId;
}
