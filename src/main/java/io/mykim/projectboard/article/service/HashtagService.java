package io.mykim.projectboard.article.service;

import io.mykim.projectboard.article.dto.response.ResponseHashtagListDto;
import io.mykim.projectboard.article.entity.Hashtag;
import io.mykim.projectboard.article.repository.ArticleHashtagRepository;
import io.mykim.projectboard.article.repository.HashtagRepository;
import io.mykim.projectboard.global.result.exception.DuplicateHashtagException;
import io.mykim.projectboard.global.result.exception.NotAllowDeleteException;
import io.mykim.projectboard.global.result.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static io.mykim.projectboard.global.result.enums.CustomErrorCode.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;
    private final ArticleHashtagRepository articleHashtagRepository;

    @Transactional(readOnly = true)
    public ResponseHashtagListDto findAllHashtags(Pageable pageable) {
        Slice<String> findHashtags = hashtagRepository.findAllNames(pageable);
        return ResponseHashtagListDto.builder()
                                        .hashtags(findHashtags.getContent())
                                        .page(findHashtags.getNumber())
                                        .hasNextPage(findHashtags.hasNext())
                                        .isLast(findHashtags.isLast())
                                        .build();
    }

    @Transactional(readOnly = true)
    public Set<Hashtag> findHashtagsByNames(Set<String> hashtagNames) {
        return new HashSet<>(hashtagRepository.findByNameIn(hashtagNames));
    }

    @Transactional
    public Set<Hashtag> renewHashtags(String requestHashtags) {
        // 1. hashtagName parsing.... #3#4..... => Set<String> [3,4]
        Set<String> requestHashtagNames = parseHashtagNames(requestHashtags);

        // 2. 기존 저장된 해시태그 확인
        Set<Hashtag> hashtags = findHashtagsByNames(requestHashtagNames);

        Set<String> existingHashtagNames = hashtags.stream()
                                                    .map(hashtag -> hashtag.getName())
                                                    .collect(Collectors.toUnmodifiableSet());

        // 3. 기존에 저장되어있지않던 해시태그는 저장한다.
        requestHashtagNames.forEach(newHashtagName -> {
            if(!existingHashtagNames.contains(newHashtagName)) {
                hashtags.add(hashtagRepository.save(Hashtag.of(newHashtagName)));
            }
        });

        return hashtags;
    }

    @Transactional
    public Long addNewHashtag(String name) {
        // 중복체크
        if(hashtagRepository.existsByName(name)) {
            throw new DuplicateHashtagException(DUPLICATE_HASHTAG_NAME);
        }

        Hashtag hashtag = Hashtag.of(name);
        return hashtagRepository.save(hashtag).getId();
    }

    @Transactional
    public void removeHashtag(Long hashtagId) {
        Hashtag hashtag = hashtagRepository.findById(hashtagId).orElseThrow(() -> new NotFoundException(NOT_FOUND_HASHTAG));

        // 사용여부 체크
        if(articleHashtagRepository.existByHashtagId(hashtagId)) {
            throw new NotAllowDeleteException(NOT_ALLOW_DELETE_HASHTAG);
        }

        hashtagRepository.delete(hashtag);
    }


    private Set<String> parseHashtagNames(String hashtags) {
        if (hashtags == null || hashtags.isEmpty() || !hashtags.contains("#")) {
            return Set.of();
        }

        Pattern pattern = Pattern.compile("#[\\w가-힣]+");
        Matcher matcher = pattern.matcher(hashtags.strip());

        Set<String> result = new LinkedHashSet<>();

        while (matcher.find()) {
            String group = matcher.group();
            String replace = group.replace("#", "");
            result.add(replace);
        }

        return Set.copyOf(result);
    }
}
