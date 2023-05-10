package io.mykim.projectboard.article.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.mykim.projectboard.article.dto.response.QResponseArticleCommentFindDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleCommentFindDto;

import javax.persistence.EntityManager;
import java.util.List;

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
                    .leftJoin(articleComment.user, user)
                    .where(articleComment.article.id.eq(articleId))
                    .orderBy(articleComment.parentArticleComment.id.asc().nullsFirst(), articleComment.createdAt.desc())
                    .fetch();
    }
}
