package io.mykim.projectboard.article.repository.querydsl;

import io.mykim.projectboard.article.entity.Hashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HashtagQuerydslRepository {
    Page<Hashtag> findAllHashtag(Pageable pageable, String keyword);
}
