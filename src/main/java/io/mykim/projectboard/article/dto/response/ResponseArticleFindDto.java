package io.mykim.projectboard.article.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import io.mykim.projectboard.article.entity.Article;
import io.mykim.projectboard.article.entity.ArticleHashTag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private String email;
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
        this.hashtags = hashtags == null ? new ArrayList<>() : Arrays.stream(hashtags.split(",")).toList();
    }


    private ResponseArticleFindDto(Article article) {
        List<ArticleHashTag> articleHashTags = article.getArticleHashTags();

        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.createdAt = article.getCreatedAt();
        this.lastModifiedAt = article.getLastModifiedAt();
        this.userId = article.getCreatedBy().getId();
        this.nickname = article.getCreatedBy().getNickname();
        this.email = article.getCreatedBy().getEmail();
        this.hashtags = articleHashTags.size() == 0 ? new ArrayList<>() : articleHashTags
                                                                            .stream()
                                                                            .map(articleHashTag -> articleHashTag.getHashtag().getName())
                                                                            .collect(Collectors.toList());
    }

    public static ResponseArticleFindDto from(Article article) {
        return new ResponseArticleFindDto(article);
    }

}
