package io.mykim.projectboard.user.exception;

import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.exception.BusinessRollbackException;
import lombok.Getter;

@Getter
public class DuplicateUserInfoException extends BusinessRollbackException {
    private CustomErrorCode errorCode;

    public DuplicateUserInfoException(CustomErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
