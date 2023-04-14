package io.mykim.projectboard.article.repository;

import io.mykim.projectboard.article.entity.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

//@RepositoryRestResource
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long>, ArticleCommentQuerydslRepository {

    // @Query : 메서드에 JPQL 쿼리 작성
    @Query("select ac from ArticleComment ac where ac.id = :articleCommentId and ac.article.id = :articleId")
    Optional<ArticleComment> findArticleCommentByIdAndArticleId(@Param("articleId")Long articleId, @Param("articleCommentId") Long articleCommentId);

}
