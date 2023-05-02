package io.mykim.projectboard.article.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import io.mykim.projectboard.article.entity.Article;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Getter
@ToString
@Setter
public class ResponseArticleFindDto {
    private Long id;
    private String title;
    private String content;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;
    private Long userId;
    private String nickname;
    private List<String> hashtags;

    @QueryProjection
    public ResponseArticleFindDto(Long id,
                                  String title,
                                  String content,
                                  LocalDateTime createdAt,
                                  LocalDateTime lastModifiedAt,
                                  Long userId,
                                  String nickname,
                                  String hashtags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.userId = userId;
        this.nickname = nickname;
        this.hashtags = Arrays.stream(hashtags.split(",")).toList();
    }



    private ResponseArticleFindDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        //this.hashtag = article.getHashtag();
        this.createdAt = article.getCreatedAt();
        this.lastModifiedAt = article.getLastModifiedAt();
        this.userId = article.getCreatedBy().getId();
        this.nickname = article.getCreatedBy().getNickname();
    }

    public static ResponseArticleFindDto of(Article article) {
        return new ResponseArticleFindDto(article);
    }


}
