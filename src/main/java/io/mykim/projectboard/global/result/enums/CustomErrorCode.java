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
    NOT_FOUND_PARENT_ARTICLE_COMMENT(404, "A003", "not found parent article comment"),


    // user
    DUPLICATE_USER_NAME(400, "U001", "duplicate username"),
    DUPLICATE_USER_NICKNAME(400, "U002", "duplicate nickname"),
    DUPLICATE_USER_EMAIL(400, "U003", "duplicate email"),

    NOT_FOUND_USER(400, "U004", "not found this username"),
    NOT_MATCH_PASSWORD(400, "U005", "not match password"),

    NOT_VALID_ACCOUNT(400, "U006", "check your account or password"),

    UN_AUTHORIZED_USER(401, "U007", "You are an unauthenticated user."),

    NOT_ALLOWED_USER(403, "U008", "You are not allowed user."),

    // hashtag
    DUPLICATE_HASHTAG_NAME(400, "H001", "duplicate hashtag"),
    NOT_FOUND_HASHTAG(400, "H002", "not found this hashtag"),
    NOT_ALLOW_DELETE_HASHTAG(400, "H003", "this hashtag is not deleted."),

    // admin
    NOT_ALLOW_ACCESS(400, "A001", "This is the port number that cannot access the api."),
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
