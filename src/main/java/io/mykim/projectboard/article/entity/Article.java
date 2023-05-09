package io.mykim.projectboard.article.entity;

import io.mykim.projectboard.article.dto.request.ArticleCreateDto;
import io.mykim.projectboard.article.dto.request.ArticleEditDto;
import io.mykim.projectboard.global.config.jpa.BaseTimeEntity;
import io.mykim.projectboard.user.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@Table(name = "article",
        indexes = {
                @Index(columnList = "article_title")
        })
public class Article extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id", nullable = false)
    private Long id;
    @Column(name = "article_title", nullable = false, length = 255)
    private String title;
    @Column(name = "article_content", nullable = false, length = 10000)
    private String content;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User createdBy;


    // [양방향 매핑]
    // Article - ArticleComment => 1 : N
    // 연관관계의 주인 : article_comment가 article_id(fk)를 갖는다
    @ToString.Exclude
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL) // CascadeType.ALL : 게시글이 지워지면 연관된 댓글도 함께 삭제
    private List<ArticleComment> articleComments = new ArrayList<>();


    // [양방향 매핑]
    // Article - ArticleHashTag => 1 : N
    // 연관관계의 주인 : article_hashtag가 article_id(fk)를 갖는다
    @ToString.Exclude
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private List<ArticleHashTag> articleHashTags = new ArrayList<>();

    private Article(ArticleCreateDto createDto) {
        this.title = createDto.getTitle();
        this.content = createDto.getContent();
    }

    public void addArticleHashTag(ArticleHashTag articleHashTag) {
        this.articleHashTags.add(articleHashTag);
        articleHashTag.setArticle(this);
    }

    public static Article createArticle(ArticleCreateDto createDto, Collection<Hashtag> hashtags) {
        Article article = new Article(createDto);

        for (Hashtag hashtag : hashtags) {
            ArticleHashTag articleHashTag = ArticleHashTag.createArticleHashTag(hashtag);
            article.addArticleHashTag(articleHashTag);
        }

        return article;
    }


    public void editArticle(ArticleEditDto editDto, Collection<Hashtag> hashtags) {
        this.title = editDto.getTitle();
        this.content = editDto.getContent();

        for (Hashtag hashtag : hashtags) {
            ArticleHashTag articleHashTag = ArticleHashTag.createArticleHashTag(hashtag);

            this.articleHashTags.add(articleHashTag);
            articleHashTag.setArticle(this);
        }
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

}
