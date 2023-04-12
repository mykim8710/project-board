package io.mykim.projectboard.global.result.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum CustomSuccessCode {
    COMMON_OK(200, "", "success"),
    INSERT_OK(200, "", "insert success"),
    UPDATE_OK(200, "", "update success"),
    DELETE_OK(200, "", "delete success"),



    ;


    private int status;
    private String code;
    private String message;

    CustomSuccessCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
