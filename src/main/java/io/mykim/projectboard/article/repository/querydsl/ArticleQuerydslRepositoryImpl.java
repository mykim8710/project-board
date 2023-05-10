package io.mykim.projectboard.article.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import io.mykim.projectboard.article.dto.request.ArticleSearchCondition;
import io.mykim.projectboard.article.dto.response.QResponseArticleFindDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleFindDto;
import io.mykim.projectboard.article.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.util.StringUtils;

import java.util.List;

import static io.mykim.projectboard.article.entity.QArticle.article;
import static io.mykim.projectboard.article.entity.QArticleHashTag.articleHashTag;
import static io.mykim.projectboard.article.entity.QHashtag.hashtag;
import static io.mykim.projectboard.user.entity.QUser.user;


public class ArticleQuerydslRepositoryImpl extends QuerydslRepositorySupport implements ArticleQuerydslRepository {
    public ArticleQuerydslRepositoryImpl() {
        super(Article.class);
    }

    @Override
    public Page<ResponseArticleFindDto> findAllArticle(Pageable pageable, ArticleSearchCondition searchCondition) {
        JPQLQuery<ResponseArticleFindDto> jpqlQuery = from(article)
                .leftJoin(article.user, user)
                .leftJoin(article.articleHashTags, articleHashTag)
                .leftJoin(articleHashTag.hashtag, hashtag)
                .groupBy(article.id)
                .having(createUniversalSearchCondition(searchCondition))
                .select(
                        new QResponseArticleFindDto(
                                article.id.as("id"),
                                article.title.as("title"),
                                article.content.as("content"),
                                article.createdAt.as("createdAt"),
                                article.lastModifiedAt.as("lastModifiedAt"),
                                article.user.id.as("userId"),
                                article.user.nickname.as("nickname"),
                                Expressions.stringTemplate("group_concat({0})", hashtag.name).as("hashtags")
                        )
                );

        JPQLQuery<ResponseArticleFindDto> result = getQuerydsl().applyPagination(pageable, jpqlQuery);
        List<ResponseArticleFindDto> content = result.fetch();

        return new PageImpl<>(content, pageable, result.fetchCount());
    }

    private BooleanExpression createUniversalSearchCondition(ArticleSearchCondition searchCondition) {
        return !StringUtils.hasLength(searchCondition.getKeyword()) ? null : chooseSearchCondition(searchCondition);
    }

    private BooleanExpression chooseSearchCondition(ArticleSearchCondition searchCondition) {
        switch (searchCondition.getSearchType()) {
            case TITLE:
                return articleTitleLike(searchCondition.getKeyword());

            case CONTENT:
                return articleContentLike(searchCondition.getKeyword());

            case NICKNAME:
                return articleCreatedByLike(searchCondition.getKeyword());
            case HASHTAG:
                return articleHashtagLike(searchCondition.getKeyword());

            default:
                return articleTitleLike(searchCondition.getKeyword())
                            .or(articleContentLike(searchCondition.getKeyword())
                                .or(articleCreatedByLike(searchCondition.getKeyword()))
                                    .or(articleHashtagLike(searchCondition.getKeyword()))
                            );
        }
    }

    private BooleanExpression articleTitleLike(String keyword) {
        return !StringUtils.hasLength(keyword) ? null : article.title.contains(keyword);
    }
    private BooleanExpression articleContentLike(String keyword) {
        return !StringUtils.hasLength(keyword) ? null : article.content.contains(keyword);
    }
    private BooleanExpression articleCreatedByLike(String keyword) {
        return !StringUtils.hasLength(keyword) ? null : article.user.nickname.contains(keyword);
    }
    private BooleanExpression articleHashtagLike(String keyword) {
        return !StringUtils.hasLength(keyword) ? null : Expressions.stringTemplate("group_concat({0})", hashtag.name).contains(keyword);
    }

}
