package io.mykim.projectboard.service;

import io.mykim.projectboard.domain.entity.Article;
import io.mykim.projectboard.dto.request.RequestArticleCreateDto;
import io.mykim.projectboard.dto.request.RequestArticleEditDto;
import io.mykim.projectboard.dto.response.ResponseArticleFindDto;
import io.mykim.projectboard.dto.response.ResponseArticleListDto;
import io.mykim.projectboard.global.pagination.CustomPaginationRequest;
import io.mykim.projectboard.global.pagination.CustomSortingRequest;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.exception.NotFoundException;
import io.mykim.projectboard.repository.ArticleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@DisplayName("ArticleService에 정의된 Article 엔티티에 대한 CRUD 비지니스 로직을 테스트한다.")
@Transactional
@SpringBootTest
class ArticleServiceTest {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("새로운 Article 생성하고 저장한다.")
    void createArticleTest() throws Exception {
        // given
        String title = "title";
        String content = "content";
        String hashtag = "hashtag";
        RequestArticleCreateDto createDto = new RequestArticleCreateDto(title, content, hashtag);

        // when
        Long articleId = articleService.createArticle(createDto);

        // then
        Article findArticle = em.find(Article.class, articleId);
        Assertions.assertThat(title).isEqualTo(findArticle.getTitle());
        Assertions.assertThat(content).isEqualTo(findArticle.getContent());
    }

    @Test
    @DisplayName("id로 해당 Article을 조회한다.")
    void findOneArticleByIdTest() throws Exception {
        // given
        String title = "title";
        String content = "content";
        String hashtag = "hashtag";
        Article article = createNewArticle(title, content, hashtag);

        // when
        ResponseArticleFindDto findArticle = articleService.findOneArticle(article.getId());

        // then
        Assertions.assertThat(findArticle).isNotNull();
        Assertions.assertThat(findArticle.getArticleTitle()).isEqualTo(title);
        Assertions.assertThat(findArticle.getArticleContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("id로 해당 Article 조회 시 존재하지 않는 Article이면 NotFoundException이 발생한다.")
    void findOneArticleByIdExceptionTest() throws Exception {
        // given
        Long notValidArticleId = -1L;

        // when & then
        Assertions.assertThatThrownBy(() -> articleService.findOneArticle(notValidArticleId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(CustomErrorCode.NOT_FOUND_ARTICLE.getMessage());
    }

    @Test
    @DisplayName("조건에 맞는 Article 목록을 조회한다.")
    void findAllArticleTest() throws Exception {
        // given
        IntStream.range(1, 31)
                .mapToObj(i -> Article.of("title"+i, "content"+i, "#hashtag"+i))
                .collect(Collectors.toList())
                .forEach(article -> {
                    em.persist(article);
                });

        em.flush();
        em.clear();

        String sort = "id,ASC";
        CustomSortingRequest customSortingRequest = new CustomSortingRequest(sort);

        int offset = 2;
        int limit = 5;
        CustomPaginationRequest customPaginationRequest = new CustomPaginationRequest(offset, limit);

        String keyword= "";

        // when
        ResponseArticleListDto result = articleService.findAllArticle(customPaginationRequest, customSortingRequest, keyword);

        // then
        Assertions.assertThat(result.getResponseArticleFindDtos().size()).isEqualTo(limit);
        Assertions.assertThat(result.getResponseArticleFindDtos().get(0).getArticleTitle()).isEqualTo("title6");
    }

    @Test
    @DisplayName("해당 id의 Article을 수정한다.")
    void editArticleTest() throws Exception {
        // given
        String title = "title";
        String content = "content";
        String hashtag = "hashtag";
        Article article = createNewArticle(title, content, hashtag);

        String newTitle = "newTitle";
        String newContent = "newContent";
        String newHashtag = "newHashtag";
        RequestArticleEditDto editDto = new RequestArticleEditDto(newTitle, newContent, newHashtag);

        // when
        articleService.editArticle(editDto, article.getId());
        em.flush();
        em.clear();

        // then
        Article findArticle = em.find(Article.class, article.getId());
        Assertions.assertThat(findArticle.getTitle()).isEqualTo(newTitle);
        Assertions.assertThat(findArticle.getContent()).isEqualTo(newContent);
        Assertions.assertThat(findArticle.getHashtag()).isEqualTo(newHashtag);
    }

    @Test
    @DisplayName("해당 id의 Article을 삭제한다.")
    void removeArticleTest() throws Exception {
        // given
        String title = "title";
        String content = "content";
        String hashtag = "hashtag";
        Article article = createNewArticle(title, content, hashtag);

        // when
        articleService.removeArticle(article.getId());
        em.flush();
        em.clear();

        // then
        Assertions.assertThatThrownBy(() -> articleService.findOneArticle(article.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(CustomErrorCode.NOT_FOUND_ARTICLE.getMessage());
    }

    private Article createNewArticle(String title, String content, String hashtag) {
        Article article = Article.of(title, content, hashtag);
        em.persist(article);
        em.flush();
        em.clear();

        return article;
    }

}