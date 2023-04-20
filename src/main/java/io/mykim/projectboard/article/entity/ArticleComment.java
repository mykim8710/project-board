package io.mykim.projectboard.article.entity;

import io.mykim.projectboard.global.config.jpa.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "article_comment",
        indexes = {
                @Index(columnList = "article_comment_content")
        })
public class ArticleComment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_comment_id", nullable = false)
    private Long id;

    @Column(name = "article_comment_content", nullable = false, length = 500)
    private String content;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

//    @ToString.Exclude
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;

    private ArticleComment(String content, Article article) {
        this.content = content;
        this.article = article;
    }

    public static ArticleComment of(String content, Article article) {
        return new ArticleComment(content, article);
    }

    public void editArticleComment(String content) {
        this.content = content;
    }

}
