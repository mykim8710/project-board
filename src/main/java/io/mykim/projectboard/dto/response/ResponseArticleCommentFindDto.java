package io.mykim.projectboard.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import io.mykim.projectboard.domain.entity.Article;
import io.mykim.projectboard.domain.entity.ArticleComment;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class ResponseArticleCommentFindDto {
    private Long articleCommentId;
    private String articleCommentContent;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime lastModifiedAt;
    private String lastModifiedBy;

    private ResponseArticleCommentFindDto(ArticleComment articleComment) {
        this.articleCommentId = articleComment.getId();
        this.articleCommentContent = articleComment.getContent();
        this.createdAt = articleComment.getCreatedAt();
        this.createdBy = articleComment.getCreatedBy();
        this.lastModifiedAt = articleComment.getLastModifiedAt();
        this.lastModifiedBy = articleComment.getLastModifiedBy();
    }

    public static ResponseArticleCommentFindDto of(ArticleComment articleComment) {
        return new ResponseArticleCommentFindDto(articleComment);
    }
}
