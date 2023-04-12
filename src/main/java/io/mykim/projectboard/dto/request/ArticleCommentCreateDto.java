package io.mykim.projectboard.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArticleCommentCreateDto {
    private String content;

    public ArticleCommentCreateDto(String content) {
        this.content = content;
    }
}
