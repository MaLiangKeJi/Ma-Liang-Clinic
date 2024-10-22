package com.clinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeficiencyStockVo {

    private String drugName;
    private String batchNumber;
    private Long number;
}
