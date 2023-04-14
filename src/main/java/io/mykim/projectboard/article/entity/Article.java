package io.mykim.projectboard.article.entity;

import io.mykim.projectboard.article.dto.request.ArticleCreateDto;
import io.mykim.projectboard.global.config.jpa.BaseTimeEntity;
import io.mykim.projectboard.article.dto.request.ArticleEditDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Entity
@Table(name = "article",
        indexes = {
                @Index(columnList = "article_title"),
                @Index(columnList = "article_hashtag")
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
    @Column(name = "article_hashtag", length = 255)
    private String hashtag;

    // [양방향 매핑]
    // article - articleComment, 1 : N
    // 연관관계의 주인 : articleComment가 articleId(fk)를 가짐
    @ToString.Exclude
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL) // CascadeType.ALL : 게시글이 지워지면 연관된 댓글도 함께 삭제
    private List<ArticleComment> articleComments = new ArrayList<>();


    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    public static Article of(ArticleCreateDto createDto) {
        return new Article(createDto.getTitle(), createDto.getContent(), createDto.getHashtag());
    }

    public void updateTitle(String title){
        this.title = title;
    }

    public void updateContent(String content){
        this.content = content;
    }

    public void updateHashtag(String hashtag){
        this.hashtag = hashtag;
    }

    public void editArticle(ArticleEditDto editDto) {
        this.title = editDto.getTitle();
        this.content = editDto.getContent();
        this.hashtag = editDto.getHashtag();
    }

//    // 양방향 편의 메서드, article - articleComment, 1 : N
//    public void addArticleComment(ArticleComment articleComment) {
//        this.articleComments.add(articleComment);
//        articleComment.setArticle(this);
//    }
}
