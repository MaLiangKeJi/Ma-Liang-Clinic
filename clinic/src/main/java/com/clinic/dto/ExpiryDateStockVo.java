package com.clinic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpiryDateStockVo {

    private String drugName;
    private String batchNumber;
    private Date expiryDate;
}
