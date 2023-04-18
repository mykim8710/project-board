package io.mykim.projectboard.article.repository;

import io.mykim.projectboard.article.dto.response.ResponseArticleCommentFindDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleCommentQuerydslRepository {
    Page<ResponseArticleCommentFindDto> findAllArticleCommentUnderArticle(Pageable pageable, Long articleId);
}
