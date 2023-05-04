package io.mykim.projectboard.article.repository.querydsl;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.mykim.projectboard.article.dto.response.QResponseArticleCommentFindDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleCommentFindDto;
import io.mykim.projectboard.article.entity.ArticleComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.selectFrom;
import static io.mykim.projectboard.article.entity.QArticleComment.articleComment;
import static io.mykim.projectboard.user.entity.QUser.user;


public class ArticleCommentQuerydslRepositoryImpl implements ArticleCommentQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public ArticleCommentQuerydslRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ResponseArticleCommentFindDto> findAllArticleCommentUnderArticle(Long articleId) {
        return queryFactory
                    .select(new QResponseArticleCommentFindDto(
                            articleComment.id.as("articleCommentId"),
                            articleComment.content.as("articleCommentContent"),
                            articleComment.createdAt.as("createdAt"),
                            articleComment.lastModifiedAt.as("lastModifiedAt"),
                            user.id.as("userId"),
                            user.nickname.as("nickname"),
                            articleComment.parentArticleComment.id.as("parentArticleCommentId")
                    ))
                    .from(articleComment)
                    .leftJoin(articleComment.createdBy, user)
                    .where(articleComment.article.id.eq(articleId))
                    .orderBy(articleComment.parentArticleComment.id.asc().nullsFirst(), articleComment.createdAt.desc())
                    .fetch();
    }
}
