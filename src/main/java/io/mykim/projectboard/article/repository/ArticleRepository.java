package io.mykim.projectboard.article.repository;

import io.mykim.projectboard.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

//@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article,Long>, ArticleQuerydslRepository {

}
