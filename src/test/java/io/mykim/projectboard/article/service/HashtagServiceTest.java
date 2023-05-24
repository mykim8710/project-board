package io.mykim.projectboard.article.service;

import io.mykim.projectboard.article.dto.response.ResponseHashtagListDto;
import io.mykim.projectboard.article.entity.ArticleHashTag;
import io.mykim.projectboard.article.entity.Hashtag;
import io.mykim.projectboard.article.repository.ArticleHashtagRepository;
import io.mykim.projectboard.article.repository.HashtagRepository;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.exception.DuplicateHashtagException;
import io.mykim.projectboard.global.result.exception.NotAllowDeleteException;
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
@SpringBootTest
class HashtagServiceTest {
    @Autowired
    private HashtagService hashtagService;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private ArticleHashtagRepository articleHashtagRepository;

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


    @Test
    @DisplayName("새로운 해시태그를 등록한다.")
    void addNewHashtagTest() throws Exception {
        // given
        String name = "blue";

        // when
        Long hashtagId = hashtagService.addNewHashtag(name);

        // then
        Assertions.assertThat(hashtagRepository.findById(hashtagId).get().getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("새로운 해시태그를 등록하는데 기존에 등록되어있는 해시태그이면 excpetion이 발생한다.")
    void addNewHashtagExceptionTest() throws Exception {
        // given
        String name = "blue";
        Hashtag hashtag = Hashtag.of(name);
        hashtagRepository.save(hashtag);
        hashtagRepository.flush();

        // when & then
        Assertions.assertThatThrownBy(() -> hashtagService.addNewHashtag(name))
                .isInstanceOf(DuplicateHashtagException.class)
                .hasMessage(CustomErrorCode.DUPLICATE_HASHTAG_NAME.getMessage());
    }

    @Test
    @DisplayName("해시태그를 삭제한다.")
    void hashtagRemoveTest() throws Exception {
        // given
        String name = "blue";
        Hashtag hashtag = Hashtag.of(name);
        Hashtag saveHashtag = hashtagRepository.save(hashtag);
        hashtagRepository.flush();

        // when
        hashtagService.removeHashtag(saveHashtag.getId());
        hashtagRepository.flush();

        // then
        Assertions.assertThat(hashtagRepository.findById(saveHashtag.getId())).isEmpty();
    }

    @Test
    @DisplayName("해시태그를 삭제할때 사용중인 해시태그이면 exception이 발생한다.")
    void hashtagRemoveExceptionTest() throws Exception {
        // given
        String name = "blue";
        Hashtag hashtag = Hashtag.of(name);
        Hashtag saveHashtag = hashtagRepository.save(hashtag);
        hashtagRepository.flush();

        ArticleHashTag articleHashTag = ArticleHashTag.createArticleHashTag(saveHashtag);
        articleHashtagRepository.save(articleHashTag);
        articleHashtagRepository.flush();

        // when & then
        Assertions.assertThatThrownBy(() -> hashtagService.removeHashtag(saveHashtag.getId()))
                .isInstanceOf(NotAllowDeleteException.class)
                .hasMessage(CustomErrorCode.NOT_ALLOW_DELETE_HASHTAG.getMessage());
    }
}