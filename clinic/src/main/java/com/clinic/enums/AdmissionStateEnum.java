package com.clinic.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AdmissionStateEnum {

    NOT_START(0, "未接诊"),
    RUN(1, "正在接诊"),
    END(2, "结束就诊");

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String msg;
}
