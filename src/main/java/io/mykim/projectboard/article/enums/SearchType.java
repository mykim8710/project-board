package io.mykim.projectboard.article.enums;

import lombok.Getter;

@Getter
public enum SearchType {
    ALL("전체"),
    TITLE("제목"),
    CONTENT("본문"),
    HASHTAG("해시태그"),
    NICKNAME("닉네임"),

    ;
    private final String description;

    SearchType(String description) {
        this.description = description;
    }
}
