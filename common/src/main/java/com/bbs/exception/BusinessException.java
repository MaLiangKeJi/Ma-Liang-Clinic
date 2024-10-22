package com.bbs.exception;

import com.bbs.enums.CodeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BusinessException extends RuntimeException {

    private Integer code;

    private String msg;

    public BusinessException(CodeEnum codeEnum) {
        code = codeEnum.getCode();
        msg = codeEnum.getMsg();
    }

    public BusinessException(String msg) {
        code = 500;
        this.msg = msg;
    }

    public static void throwException(String msg) throws BusinessException {
        throw new BusinessException(401, msg);
    }
}
