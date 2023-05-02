package io.mykim.projectboard.article.service;

import io.mykim.projectboard.article.dto.response.ResponseHashtagListDto;
import io.mykim.projectboard.article.entity.Hashtag;
import io.mykim.projectboard.article.repository.HashtagRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@DisplayName("HashtagService에 정의된 비지니스로직들을 테스트한다.")
@Slf4j
@Transactional
@SpringBootTest(properties = {"JASYPT_SECRET_KEY=test"})
class HashtagServiceTest {
    @Autowired
    private HashtagService hashtagService;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Test
    @DisplayName("저장된 모든 해시태그를 조회(+pagination)한다.")
    void findAllHashtagsTest() throws Exception {
        // given
        List<Hashtag> insertHashtags = IntStream.range(1, 30)
                                            .mapToObj(i -> Hashtag.of("hashtag_" + i))
                                            .collect(Collectors.toList());

        hashtagRepository.saveAll(insertHashtags);

        int page = 0;
        int size = 5;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

        // when
        ResponseHashtagListDto responseHashtagListDto = hashtagService.findAllHashtags(pageRequest);
        List<String> hashtags = responseHashtagListDto.getHashtags();

        // then
        Assertions.assertThat(hashtags.size()).isEqualTo(size);
    }
}