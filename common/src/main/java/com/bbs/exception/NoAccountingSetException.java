package com.bbs.exception;

import com.bbs.enums.CodeEnum;

import static com.bbs.enums.CodeEnum.FAILED_USER_LOGIN_EXPIRE;

public class NoAccountingSetException extends BusinessException {

    public NoAccountingSetException() {
        super(FAILED_USER_LOGIN_EXPIRE);
    }

    public NoAccountingSetException(CodeEnum codeEnum) {
        super(codeEnum);
    }
}
