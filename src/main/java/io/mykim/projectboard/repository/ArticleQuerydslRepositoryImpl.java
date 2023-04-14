package io.mykim.projectboard.repository;


import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.mykim.projectboard.domain.entity.Article;
import io.mykim.projectboard.dto.request.ArticleSearchCondition;
import io.mykim.projectboard.dto.response.QResponseArticleFindDto;
import io.mykim.projectboard.dto.response.ResponseArticleFindDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static io.mykim.projectboard.domain.entity.QArticle.article;

public class ArticleQuerydslRepositoryImpl implements ArticleQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public ArticleQuerydslRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<ResponseArticleFindDto> findAllArticle(Pageable pageable, ArticleSearchCondition searchCondition) {
        List<ResponseArticleFindDto> responseArticleFindDtos = queryFactory
                                                                    .select(new QResponseArticleFindDto(
                                                                            article.id.as("articleId"),
                                                                            article.title.as("articleTitle"),
                                                                            article.content.as("articleContent"),
                                                                            article.hashtag.as("articleHashtag"),
                                                                            article.createdAt.as("articleCreateAt"),
                                                                            article.createdBy.as("articleCreatedBy"),
                                                                            article.lastModifiedAt.as("articleLastModifiedAt"),
                                                                            article.lastModifiedBy.as("articleLastModifiedBy")))
                                                                    .from(article)
                                                                    .where(createUniversalSearchCondition(searchCondition))
                                                                    .offset(pageable.getOffset())
                                                                    .limit(pageable.getPageSize())
                                                                    .orderBy(getOrderSpecifier(pageable.getSort())
                                                                            .stream()
                                                                            .toArray(OrderSpecifier[]::new))
                                                                    .fetch();

        Long count = queryFactory
                            .select(article.count())
                            .from(article)
                            .where(createUniversalSearchCondition(searchCondition))
                            .fetchOne();

        return new PageImpl<>(responseArticleFindDtos, pageable, count);
    }


    private BooleanExpression createUniversalSearchCondition(ArticleSearchCondition searchCondition) {
        return !StringUtils.hasLength(searchCondition.getKeyword()) ? null : chooseSearchCondition(searchCondition);
    }

    private BooleanExpression chooseSearchCondition(ArticleSearchCondition searchCondition) {
        switch (searchCondition.getSearchType()) {
            case "T" :  // title
                return articleTitleLike(searchCondition.getKeyword());

            case "C" :  // content
                return articleContentLike(searchCondition.getKeyword());

            case "H" :  // hashtag
                return articleHashtagLike(searchCondition.getKeyword());

            case "N" :  // createdBy(User Nickname)
                return articleCreatedByLike(searchCondition.getKeyword());

            default:    // A : Universal search
                return articleTitleLike(searchCondition.getKeyword())
                        .or(articleContentLike(searchCondition.getKeyword())
                                .or(articleHashtagLike(searchCondition.getKeyword())
                                        .or(articleCreatedByLike(searchCondition.getKeyword()))
                                )
                        );
        }
    }

    private BooleanExpression articleTitleLike(String keyword) {
        return !StringUtils.hasLength(keyword) ? null : article.title.contains(keyword);
    }

    private BooleanExpression articleContentLike(String keyword) {
        return !StringUtils.hasLength(keyword) ? null : article.content.contains(keyword);
    }

    private BooleanExpression articleHashtagLike(String keyword) {
        return !StringUtils.hasLength(keyword) ? null : article.hashtag.contains(keyword);
    }

    private BooleanExpression articleCreatedByLike(String keyword) {
        return !StringUtils.hasLength(keyword) ? null : article.createdBy.contains(keyword);
    }

    private List<OrderSpecifier> getOrderSpecifier(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach (order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String orderProperty = order.getProperty();

            PathBuilder orderByExpression = new PathBuilder(Article.class, "article");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(orderProperty)));
        });

        return orders;
    }
}