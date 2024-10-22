package com.bbs.exception;

import com.bbs.enums.CodeEnum;

import static com.bbs.enums.CodeEnum.*;

public class ReLoginException extends BusinessException {

    public ReLoginException() {
        super(FAILED_USER_LOGIN_EXPIRE);
    }

    public ReLoginException(CodeEnum codeEnum) {
        super(codeEnum);
    }
}
