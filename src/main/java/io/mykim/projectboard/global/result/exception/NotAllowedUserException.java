package io.mykim.projectboard.global.result.exception;

import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import lombok.Getter;

@Getter
public class NotAllowedUserException extends BusinessRollbackException {
    private CustomErrorCode errorCode;

    public NotAllowedUserException(CustomErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
