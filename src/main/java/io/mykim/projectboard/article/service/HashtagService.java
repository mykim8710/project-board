package io.mykim.projectboard.article.service;

import io.mykim.projectboard.article.dto.response.ResponseHashtagListDto;
import io.mykim.projectboard.article.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Slf4j
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;

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

}
