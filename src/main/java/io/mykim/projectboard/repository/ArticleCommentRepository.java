package io.mykim.projectboard.repository;

import io.mykim.projectboard.domain.entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

//@RepositoryRestResource
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
}
