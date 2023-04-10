package io.mykim.projectboard.global.result.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum CustomErrorCode {
    // validation error
    VALIDATION_ERROR(400, "V001", "validation error"),



    // article
    NOT_FOUND_ARTICLE(404, "A001", "not found this article"),

    ;


    private int status;
    private String code;
    private String message;

    CustomErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
