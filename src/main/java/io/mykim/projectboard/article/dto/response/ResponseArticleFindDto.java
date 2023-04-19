package io.mykim.projectboard.article.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import io.mykim.projectboard.article.entity.Article;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@Setter
public class ResponseArticleFindDto {
    private Long id;
    private String title;
    private String content;
    private String hashtag;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;
    private String lastModifiedBy;


    private ResponseArticleFindDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.hashtag = article.getHashtag();
        this.createdAt = article.getCreatedAt();
        this.createdBy = article.getCreatedBy();
        this.lastModifiedAt = article.getLastModifiedAt();
        this.lastModifiedBy = article.getLastModifiedBy();
    }

    public static ResponseArticleFindDto of(Article article) {
        return new ResponseArticleFindDto(article);
    }

    @QueryProjection
    public ResponseArticleFindDto(Long id, String title, String content, String hashtag, LocalDateTime createdAt, String createdBy, LocalDateTime lastModifiedAt, String lastModifiedBy) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.lastModifiedAt = lastModifiedAt;
        this.lastModifiedBy = lastModifiedBy;
    }
}
