package io.mykim.projectboard.article.repository;


import io.mykim.projectboard.article.dto.request.ArticleSearchCondition;
import io.mykim.projectboard.article.dto.response.ResponseArticleFindDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleQuerydslRepository {
    Page<ResponseArticleFindDto> findAllArticle(Pageable pageable, ArticleSearchCondition searchCondition);
}
