package io.mykim.projectboard.global.result.exception;

import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import lombok.Getter;

@Getter
public class DuplicateHashtagException extends BusinessRollbackException {
    private CustomErrorCode errorCode;

    public DuplicateHashtagException(CustomErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
