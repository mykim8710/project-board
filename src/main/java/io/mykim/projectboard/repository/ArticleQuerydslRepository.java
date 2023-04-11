package io.mykim.projectboard.repository;


import io.mykim.projectboard.dto.request.ArticleSearchCondition;
import io.mykim.projectboard.dto.response.ResponseArticleFindDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleQuerydslRepository {
    Page<ResponseArticleFindDto> findAllArticle(Pageable pageable, ArticleSearchCondition searchCondition);
}
