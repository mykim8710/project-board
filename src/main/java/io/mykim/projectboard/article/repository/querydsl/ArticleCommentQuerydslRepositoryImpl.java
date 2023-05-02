package io.mykim.projectboard.article.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import io.mykim.projectboard.article.dto.response.QResponseArticleCommentFindDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleCommentFindDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
    public Page<ResponseArticleCommentFindDto> findAllArticleCommentUnderArticle(Pageable pageable, Long articleId) {
        List<ResponseArticleCommentFindDto> articleCommentFindDtos = queryFactory
                                                                        .select(new QResponseArticleCommentFindDto(
                                                                                articleComment.id.as("articleCommentId"),
                                                                                articleComment.content.as("articleCommentContent"),
                                                                                articleComment.createdAt.as("createdAt"),
                                                                                articleComment.lastModifiedAt.as("lastModifiedAt"),
                                                                                articleComment.createdBy.id.as("userId"),
                                                                                articleComment.createdBy.nickname.as("nickname")
                                                                        ))
                                                                        .from(articleComment)
                                                                        .leftJoin(articleComment.createdBy, user)
                                                                        .where(articleComment.article.id.eq(articleId))
                                                                        .offset(pageable.getOffset())
                                                                        .limit(pageable.getPageSize())
                                                                        .orderBy(articleComment.id.desc())
                                                                        .fetch();

        Long count = queryFactory
                        .select(articleComment.count())
                        .from(articleComment)
                        .where(articleComment.article.id.eq(articleId))
                        .fetchOne();

        return new PageImpl<>(articleCommentFindDtos, pageable, count);
    }
}
