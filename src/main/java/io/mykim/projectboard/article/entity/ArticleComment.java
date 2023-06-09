package io.mykim.projectboard.article.entity;

import io.mykim.projectboard.global.jpa.BaseTimeEntity;
import io.mykim.projectboard.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "article_comment",
        indexes = {
                @Index(columnList = "article_comment_content")
        })
public class ArticleComment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_comment_id", nullable = false)
    private Long id;

    @Column(name = "article_comment_content", nullable = false, length = 500)
    private String content;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User user;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_article_comment_id", updatable = false, nullable = true)
    private ArticleComment parentArticleComment;

    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "parentArticleComment", cascade = CascadeType.REMOVE) // CascadeType.ALL : 해당 댓글이 지워지원 자식댓글들도 다 지워짐
    private Set<ArticleComment> childArticleComment = new LinkedHashSet<>();

    private ArticleComment(String content, Article article, User user, ArticleComment parentArticleComment) {
        this.content = content;
        this.article = article;
        this.user = user;
        this.parentArticleComment = parentArticleComment;
    }

    public static ArticleComment createArticleComment(String content, Article article, User user) {
        return new ArticleComment(content, article, user, null);
    }

    public void editArticleComment(String content) {
        this.content = content;
    }

    public void addChildArticleComment(ArticleComment childArticleComment) {
        childArticleComment.setParentArticleComment(this);
        this.getChildArticleComment().add(childArticleComment);
    }

    public void setParentArticleComment(ArticleComment parentArticleComment) {
        this.parentArticleComment = parentArticleComment;
    }
}
