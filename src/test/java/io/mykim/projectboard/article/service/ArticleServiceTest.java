package io.mykim.projectboard.article.service;

import io.mykim.projectboard.article.dto.request.ArticleCreateDto;
import io.mykim.projectboard.article.dto.request.ArticleEditDto;
import io.mykim.projectboard.article.dto.request.ArticleSearchCondition;
import io.mykim.projectboard.article.dto.response.ResponseArticleFindDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleListDto;
import io.mykim.projectboard.article.entity.Article;
import io.mykim.projectboard.article.repository.ArticleRepository;
import io.mykim.projectboard.config.WithAuthUser;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.exception.NotFoundException;
import io.mykim.projectboard.global.select.pagination.CustomPaginationRequest;
import io.mykim.projectboard.global.select.sort.CustomSortingRequest;
import io.mykim.projectboard.user.dto.request.UserCreateDto;
import io.mykim.projectboard.user.entity.User;
import io.mykim.projectboard.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@DisplayName("ArticleService에 정의된 Article 엔티티에 대한 CRUD 비지니스 로직을 테스트한다.")
@Transactional
@SpringBootTest
class ArticleServiceTest {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("새로운 Article 생성하고 저장한다.")
    @WithAuthUser(username = "test")
    void createArticleTest() throws Exception {
        // given
        String title = "title";
        String content = "content";
        String hashtag = "hashtag";
        ArticleCreateDto createDto = new ArticleCreateDto(title, content, hashtag);

        // when
        Long articleId = articleService.createArticle(createDto);

        // then
        Article findArticle = em.find(Article.class, articleId);
        Assertions.assertThat(title).isEqualTo(findArticle.getTitle());
        Assertions.assertThat(content).isEqualTo(findArticle.getContent());
    }

    @Test
    @DisplayName("id로 해당 Article을 조회한다.")
    @WithAuthUser(username = "test")
    void findOneArticleByIdTest() throws Exception {
        // given
        String title = "title";
        String content = "content";
        Article newArticle = createNewArticle(title, content);

        // when
        ResponseArticleFindDto findArticle = articleService.findOneArticle(newArticle.getId());

        // then
        Assertions.assertThat(findArticle).isNotNull();
        Assertions.assertThat(findArticle.getTitle()).isEqualTo(title);
        Assertions.assertThat(findArticle.getContent()).isEqualTo(content);
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
    @WithAuthUser(username = "test")
    void findAllArticleTest() throws Exception {
        // given
        createUser();

        IntStream.range(1, 31)
                .forEach(i -> createNewArticle("title"+i, "content"+i));

        String sort = "id_ASC";
        CustomSortingRequest customSortingRequest = new CustomSortingRequest(sort);

        int offset = 2;
        int limit = 5;
        CustomPaginationRequest customPaginationRequest = new CustomPaginationRequest(offset, limit);

        String keyword = "";
        String searchType = "A";
        ArticleSearchCondition articleSearchCondition = new ArticleSearchCondition(keyword, searchType);

        // when
        ResponseArticleListDto result = articleService.findAllArticle(customPaginationRequest, customSortingRequest, articleSearchCondition);

        // then
        Assertions.assertThat(result.getResponseArticleFindDtos().size()).isEqualTo(limit);
        Assertions.assertThat(result.getResponseArticleFindDtos().get(0).getTitle()).isEqualTo("title6");
    }

    @Test
    @DisplayName("해당 id의 Article을 수정한다.")
    @WithAuthUser(username = "test")
    void editArticleTest() throws Exception {
        // given
        String title = "title";
        String content = "content";
        String hashtag = "hashtag";
        Article article = createNewArticle(title, content);

        String newTitle = "newTitle";
        String newContent = "newContent";
        String newHashtag = "newHashtag";
        ArticleEditDto editDto = new ArticleEditDto(article.getId(), newTitle, newContent, newHashtag);

        // when
        articleService.editArticle(editDto, article.getId());

        // then
        Article findArticle = em.find(Article.class, article.getId());

        Assertions.assertThat(findArticle.getTitle()).isEqualTo(newTitle);
        Assertions.assertThat(findArticle.getContent()).isEqualTo(newContent);
    }

    @Test
    @DisplayName("해당 id의 Article을 삭제한다.")
    @WithAuthUser(username = "test")
    void removeArticleTest() throws Exception {
        // given
        String title = "title";
        String content = "content";
        Article article = createNewArticle(title, content);

        // when
        articleService.removeArticle(article.getId());

        // then
        Assertions.assertThatThrownBy(() -> articleService.findOneArticle(article.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(CustomErrorCode.NOT_FOUND_ARTICLE.getMessage());
    }

    private Article createNewArticle(String title, String content) {
        Article article = Article.of(title, content);
        return articleRepository.save(article);
    }

    private User createUser() {
        String username = "test";
        String password = "1234";
        String email = "email@eamil.com";
        String nickname = "nickname";
        String memo = "memo";

        UserCreateDto createDto = UserCreateDto.builder()
                                                .username(username)
                                                .password(password)
                                                .email(email)
                                                .nickname(nickname)
                                                .memo(memo)
                                                .build();

        User user = User.of(createDto);
        return userRepository.save(user);
    }
}