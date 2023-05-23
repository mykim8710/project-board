package io.mykim.projectboard.article.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static io.mykim.projectboard.article.entity.QArticleHashTag.articleHashTag;

public class ArticleHashtagQuerydslRepositoryImpl implements ArticleHashtagQuerydslRepository{
    private final JPAQueryFactory queryFactory;

    public ArticleHashtagQuerydslRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public boolean existByHashtagId(Long hashtagId) {
        Integer fetchOne = queryFactory
                            .selectOne()
                            .from(articleHashTag)
                            .where(articleHashTag.hashtag.id.eq(hashtagId))
                            .fetchFirst();
        return fetchOne != null;
    }
}
