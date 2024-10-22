package com.clinic.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public enum PayStateEnum {

    NO_PAY(0, "未支付"),
    IS_PAY(1, "已支付")
    ;

    private final Integer code;

    private final String msg;

    public static final Map<String, SterilizeLocationEnum> map = EnumUtil.getEnumMap(SterilizeLocationEnum.class);

    public static String getMsgByCode(Integer code){
        for (PayStateEnum obj : PayStateEnum.values()) {
            if(obj.getCode().intValue() == code.intValue()){
                return obj.getMsg();
            }
        }
        return null;
    }
}
