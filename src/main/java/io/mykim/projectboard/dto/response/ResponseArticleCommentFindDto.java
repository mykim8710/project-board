package io.mykim.projectboard.dto.response;

import com.querydsl.core.annotations.QueryProjection;
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

    @QueryProjection
    public ResponseArticleCommentFindDto(Long articleCommentId, String articleCommentContent, LocalDateTime createdAt, String createdBy, LocalDateTime lastModifiedAt, String lastModifiedBy) {
        this.articleCommentId = articleCommentId;
        this.articleCommentContent = articleCommentContent;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.lastModifiedAt = lastModifiedAt;
        this.lastModifiedBy = lastModifiedBy;
    }
}
