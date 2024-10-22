package com.clinic.app.job;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PatientCallbackTypeEnum {

    ADMISSION(0, "接诊回访"),
    ;

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String msg;
}
