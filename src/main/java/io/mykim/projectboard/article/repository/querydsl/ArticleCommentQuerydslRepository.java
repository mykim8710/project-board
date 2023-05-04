package io.mykim.projectboard.article.repository.querydsl;

import io.mykim.projectboard.article.dto.response.ResponseArticleCommentFindDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ArticleCommentQuerydslRepository {
    List<ResponseArticleCommentFindDto> findAllArticleCommentUnderArticle(Long articleId);
}
