package io.mykim.projectboard.article.dto.response;

import io.mykim.projectboard.article.entity.Article;
import io.mykim.projectboard.article.entity.ArticleHashTag;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@Setter
public class ResponseArticleForEditDto {
    private Long id;
    private String title;
    private String content;
    private Long userId;
    private String hashtags;

    private ResponseArticleForEditDto(Article article) {
        List<ArticleHashTag> articleHashTags = article.getArticleHashTags();
        this.id = article.getId();
        this.title = article.getTitle();
        this.content = article.getContent();
        this.userId = article.getUser().getId();
        this.hashtags = articleHashTags.size() == 0 ? "" : articleHashTags
                                                                .stream()
                                                                .map(articleHashTag -> "#".concat(articleHashTag.getHashtag().getName()))
                                                                .collect(Collectors.joining());
    }

    public static ResponseArticleForEditDto from(Article article) {
        return new ResponseArticleForEditDto(article);
    }

    public ResponseArticleForEditDto(Long id, String title, String content, Long userId, String hashtags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.hashtags = hashtags;
    }
}
