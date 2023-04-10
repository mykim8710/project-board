package io.mykim.projectboard.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import io.mykim.projectboard.domain.entity.Article;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
public class ResponseArticleFindDto {
    private Long articleId;
    private String articleTitle;
    private String articleContent;
    private String articleHashtag;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime lastModifiedAt;
    private String lastModifiedBy;


    private ResponseArticleFindDto(Article article) {
        this.articleId = article.getId();
        this.articleTitle = article.getTitle();
        this.articleContent = article.getContent();
        this.articleHashtag = article.getHashtag();
        this.createdAt = article.getCreatedAt();
        this.createdBy = article.getCreatedBy();
        this.lastModifiedAt = article.getLastModifiedAt();
        this.lastModifiedBy = article.getLastModifiedBy();
    }

    public static ResponseArticleFindDto of(Article article) {
        return new ResponseArticleFindDto(article);
    }

    @QueryProjection
    public ResponseArticleFindDto(Long articleId, String articleTitle, String articleContent, String articleHashtag, LocalDateTime createdAt, String createdBy, LocalDateTime lastModifiedAt, String lastModifiedBy) {
        this.articleId = articleId;
        this.articleTitle = articleTitle;
        this.articleContent = articleContent;
        this.articleHashtag = articleHashtag;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.lastModifiedAt = lastModifiedAt;
        this.lastModifiedBy = lastModifiedBy;
    }
}
