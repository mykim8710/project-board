package io.mykim.projectboard.article.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ArticleCommentCreateDto {

    private Long parentArticleCommentId;
    private String content;

    public ArticleCommentCreateDto(Long parentArticleCommentId, String content) {
        this.parentArticleCommentId = parentArticleCommentId;
        this.content = content;
    }
}
