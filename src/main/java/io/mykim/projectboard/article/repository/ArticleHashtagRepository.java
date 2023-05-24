package io.mykim.projectboard.article.repository;

import io.mykim.projectboard.article.entity.ArticleHashTag;
import io.mykim.projectboard.article.repository.querydsl.ArticleHashtagQuerydslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ArticleHashtagRepository extends JpaRepository<ArticleHashTag, Long>, ArticleHashtagQuerydslRepository {
    void deleteAllByArticleId(Long articleId);
}
