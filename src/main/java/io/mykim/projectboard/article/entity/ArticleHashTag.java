package io.mykim.projectboard.article.entity;

import lombok.*;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    // Hashtag - ArticleHashTag => 1 : N
    // 연관관계의 주인 : article_hashtag가 hashtag_id(fk)를 갖는다
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    private ArticleHashTag(Hashtag hashtag) {
        this.hashtag = hashtag;
    }

    public static ArticleHashTag createArticleHashTag(Hashtag hashtag) {
        return new ArticleHashTag(hashtag);
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
