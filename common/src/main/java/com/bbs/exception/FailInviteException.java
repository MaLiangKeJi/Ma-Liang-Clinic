package com.bbs.exception;

import com.bbs.enums.CodeEnum;

import static com.bbs.enums.CodeEnum.FAILED_REG_INVITE_NOT_AVAILABLE;

public class FailInviteException extends BusinessException {

    public FailInviteException() {
        super(FAILED_REG_INVITE_NOT_AVAILABLE);
    }

    public FailInviteException(CodeEnum codeEnum) {
        super(codeEnum);
    }
}