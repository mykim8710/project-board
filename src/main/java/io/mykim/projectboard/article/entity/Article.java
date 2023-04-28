package io.mykim.projectboard.article.entity;

import io.mykim.projectboard.article.dto.request.ArticleCreateDto;
import io.mykim.projectboard.article.dto.request.ArticleEditDto;
import io.mykim.projectboard.global.config.jpa.BaseEntity;
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
                @Index(columnList = "article_title")
        })
public class Article extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_id", nullable = false)
    private Long id;
    @Column(name = "article_title", nullable = false, length = 255)
    private String title;
    @Column(name = "article_content", nullable = false, length = 10000)
    private String content;


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
    @OneToMany(mappedBy = "article", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private List<ArticleHashTag> articleHashTags = new ArrayList<>();


    private Article(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Article of(String title, String content) {
        return new Article(title, content);
    }

    public static Article of(ArticleCreateDto createDto) {
        return new Article(createDto.getTitle(), createDto.getContent());
    }

    public void updateTitle(String title){
        this.title = title;
    }

    public void updateContent(String content){
        this.content = content;
    }


    public void editArticle(ArticleEditDto editDto) {
        this.title = editDto.getTitle();
        this.content = editDto.getContent();
    }
}
