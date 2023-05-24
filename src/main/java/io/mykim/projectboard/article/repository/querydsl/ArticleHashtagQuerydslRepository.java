package io.mykim.projectboard.article.repository.querydsl;

public interface ArticleHashtagQuerydslRepository {
    boolean existByHashtagId(Long hashtagId);
}
