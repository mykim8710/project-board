package io.mykim.projectboard.article.repository.querydsl;


import io.mykim.projectboard.global.dto.SearchCondition;
import io.mykim.projectboard.article.dto.response.ResponseArticleFindDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArticleQuerydslRepository {
    Page<ResponseArticleFindDto> findAllArticle(Pageable pageable, SearchCondition searchCondition);
}
