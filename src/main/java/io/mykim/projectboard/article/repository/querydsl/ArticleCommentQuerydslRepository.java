package io.mykim.projectboard.article.repository.querydsl;

import io.mykim.projectboard.global.dto.SearchCondition;
import io.mykim.projectboard.article.dto.response.ResponseArticleCommentFindDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleFindDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleCommentQuerydslRepository {
    List<ResponseArticleCommentFindDto> findAllArticleCommentUnderArticle(Long articleId);

    Page<ResponseArticleFindDto> findAllArticleComment(Pageable pageable, SearchCondition searchCondition);
}
