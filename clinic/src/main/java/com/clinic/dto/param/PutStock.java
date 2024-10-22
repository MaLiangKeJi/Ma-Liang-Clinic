package com.clinic.dto.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PutStock {

    @NotNull
    private Long id;

    @NotNull
    private Long number;

    @NotNull
    private Long unitId;
}
