package io.mykim.projectboard.article.repository.querydsl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.mykim.projectboard.article.entity.Hashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static io.mykim.projectboard.article.entity.QHashtag.hashtag;
import static io.mykim.projectboard.global.jpa.QuerydslUtils.getOrderSpecifier;

public class HashtagQuerydslRepositoryImpl implements  HashtagQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public HashtagQuerydslRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Hashtag> findAllHashtag(Pageable pageable, String keyword) {
        List<Hashtag> hashtags = queryFactory
                                    .selectFrom(hashtag)
                                    .where(createUniversalSearchCondition(keyword))
                                    .orderBy(getOrderSpecifier(pageable.getSort(), hashtag.getType(), hashtag.getMetadata())
                                            .stream()
                                            .toArray(OrderSpecifier[]::new))
                                    .offset(pageable.getOffset())
                                    .limit(pageable.getPageSize())
                                    .fetch();

        Long count = queryFactory
                            .select(hashtag.count())
                            .from(hashtag)
                            .where(createUniversalSearchCondition(keyword))
                            .fetchOne();

        return new PageImpl<>(hashtags, pageable, count);
    }

    private BooleanExpression createUniversalSearchCondition(String keyword) {
        return !StringUtils.hasLength(keyword) ? null : hashtag.name.contains(keyword);
    }
}
