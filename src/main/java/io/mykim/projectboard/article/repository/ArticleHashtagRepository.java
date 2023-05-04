package io.mykim.projectboard.article.repository;

import io.mykim.projectboard.article.entity.ArticleHashTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleHashtagRepository extends JpaRepository<ArticleHashTag, Long> {
    void deleteAllByArticleId(Long articleId);
}
