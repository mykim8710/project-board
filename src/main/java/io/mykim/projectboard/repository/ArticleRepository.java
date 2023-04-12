package io.mykim.projectboard.repository;

import io.mykim.projectboard.domain.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

//@RepositoryRestResource
public interface ArticleRepository extends JpaRepository<Article,Long>, ArticleQuerydslRepository {

}
