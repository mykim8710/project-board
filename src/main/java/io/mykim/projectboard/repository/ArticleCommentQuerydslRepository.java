package io.mykim.projectboard.repository;

import io.mykim.projectboard.dto.response.ResponseArticleCommentFindDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleCommentQuerydslRepository {
    Page<ResponseArticleCommentFindDto> findAllArticleCommentUnderArticle(Pageable pageable, Long articleId, String keyword);
}
