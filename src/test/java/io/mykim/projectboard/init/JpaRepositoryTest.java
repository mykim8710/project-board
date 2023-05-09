package io.mykim.projectboard.init;

import io.mykim.projectboard.article.dto.request.ArticleCreateDto;
import io.mykim.projectboard.article.entity.Hashtag;
import io.mykim.projectboard.article.entity.Article;
import io.mykim.projectboard.article.repository.ArticleCommentRepository;
import io.mykim.projectboard.article.repository.ArticleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Disabled("초기 설정 시 진행했던 테스트이므로 개발을 진행됨에 따라 해당 테스트는 진행안함")
@DisplayName("JPA 연결 테스트")
@Import(JpaConfig.class)  // auding 관련 config import
@DataJpaTest
// JPA에 관련된 요소들만 테스트하기 위한 어노테이션으로 JPA 테스트에 관련된 설정들만 적용
// 메모리상에 내부 데이터베이스를 생성하고 @Entity 클래스들을 등록하고 JPA Repository 설정들을 해준다.
// 각 테스트마다 테스트가 완료되면 관련한 설정들은 롤백
class JpaRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleCommentRepository articleCommentRepository;

    @Test
    @DisplayName("select 테스트")
    void articleSelectTest() throws Exception{
        // given


        // when
        List<Article> articles = articleRepository.findAll();

        // then
        Assertions.assertThat(articles)
                .isNotNull()
                .hasSize(100);
    }

    @Test
    @DisplayName("insert 테스트")
    void articleInsertTest() throws Exception{
        // given
        long previousCount = articleRepository.count();

        String title = "title";
        String content = "content";
        Article article = createArticle(title, content);

        // when
        articleRepository.save(article);

        // then
        Assertions.assertThat(articleRepository.count())
                            .isEqualTo(previousCount+1);
    }

    @Test
    @DisplayName("update 테스트")
    void articleUpdateTest() throws Exception{
        // given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updateTitle = "updateTitle";
        String updateContent = "updateContent";
        String updateHashtag = "#updateHashtag#RED";

        // when
        article.updateTitle(updateTitle);
        article.updateContent(updateContent);

        articleRepository.flush();

        // then
        Article findArticle = articleRepository.findById(1L).orElseThrow();
        Assertions.assertThat(findArticle.getTitle()).isEqualTo(updateTitle);
        Assertions.assertThat(findArticle.getContent()).isEqualTo(updateContent);
    }

    @Test
    @DisplayName("delete 테스트")
    void articleDeleteTest() throws Exception{
        // given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        long previousArticleCommentCount = articleCommentRepository.count();
        int deletedCommentCount = article.getArticleComments().size();

        // when
        articleRepository.delete(article);

        // then
        Assertions.assertThat(articleRepository.count()).isEqualTo(previousArticleCount - 1);
        Assertions.assertThat(articleCommentRepository.count()).isEqualTo(previousArticleCommentCount - deletedCommentCount);
    }


    private Article createArticle(String title, String content) {
        ArticleCreateDto articleCreateDto = new ArticleCreateDto(title, content);
        Set<Hashtag> hashtags = IntStream.range(1, 10)
                                        .mapToObj(i-> Hashtag.of("blue_"+i))
                                        .collect(Collectors.toUnmodifiableSet());

        return Article.createArticle(articleCreateDto, hashtags);
    }
}