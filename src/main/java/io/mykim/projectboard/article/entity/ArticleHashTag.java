package io.mykim.projectboard.article.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "article_hashtag")
public class ArticleHashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "article_hashtag_id")
    private Long id;

    // Article - ArticleHashTag => 1 : N
    // 연관관계의 주인 : article_hashtag가 article_id(fk)를 갖는다
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    // Hashtag - ArticleHashTag => 1 : N
    // 연관관계의 주인 : article_hashtag가 hashtag_id(fk)를 갖는다
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    public ArticleHashTag(Long id, Article article, Hashtag hashtag) {
        this.id = id;
        this.article = article;
        this.hashtag = hashtag;
    }
}
