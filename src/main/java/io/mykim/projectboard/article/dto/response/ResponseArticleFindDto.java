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
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;

    private Long userId;
    private String nickname;

    private ResponseArticleFindDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.hashtag = article.getHashtag();
        this.createdAt = article.getCreatedAt();
        this.lastModifiedAt = article.getLastModifiedAt();
        this.userId = article.getCreatedBy().getId();
        this.nickname = article.getCreatedBy().getNickname();
    }

    public static ResponseArticleFindDto of(Article article) {
        return new ResponseArticleFindDto(article);
    }

    @QueryProjection
    public ResponseArticleFindDto(Long id,
                                  String title, String content, String hashtag,
                                  LocalDateTime createdAt, LocalDateTime lastModifiedAt,
                                  Long userId, String nickname) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.userId = userId;
        this.nickname = nickname;
    }
}
