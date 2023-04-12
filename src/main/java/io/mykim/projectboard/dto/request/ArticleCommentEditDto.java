package io.mykim.projectboard.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArticleCommentEditDto {
    private String content;

    public ArticleCommentEditDto(String content) {
        this.content = content;
    }
}
