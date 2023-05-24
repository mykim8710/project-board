package io.mykim.projectboard.article.repository;

import io.mykim.projectboard.article.entity.Hashtag;
import io.mykim.projectboard.article.repository.querydsl.HashtagQuerydslRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface HashtagRepository extends JpaRepository<Hashtag, Long>, HashtagQuerydslRepository {
    @Query("select h.name from Hashtag h")
    Slice<String> findAllNames(Pageable pageable);

    List<Hashtag> findByNameIn(Set<String> hashtagNames);

    boolean existsByName(String name);
}
