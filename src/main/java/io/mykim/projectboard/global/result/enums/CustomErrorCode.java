package io.mykim.projectboard.global.result.enums;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum CustomErrorCode {
    // common
    NOT_VALID_REQUEST(400, "G001", "not validation request"),

    // validation error
    VALIDATION_ERROR(400, "V001", "validation error"),



    // article, articleComment
    NOT_FOUND_ARTICLE(404, "A001", "not found this article"),
    NOT_FOUND_ARTICLE_COMMENT(404, "A002", "not found this article comment"),


    // user
    DUPLICATE_USER_NAME(400, "U001", "duplicate username"),
    DUPLICATE_USER_NICKNAME(400, "U002", "duplicate nickname"),
    DUPLICATE_USER_EMAIL(400, "U003", "duplicate email"),

    NOT_FOUND_USER(400, "U004", "not found this username"),

    NOT_MATCH_PASSWORD(400, "U005", "not match password"),

    UN_AUTHORIZED_USER(401, "U006", "You are an unauthenticated user."),

    NOT_ALLOWED_USER(403, "U007", "You are not allowed user."),
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
