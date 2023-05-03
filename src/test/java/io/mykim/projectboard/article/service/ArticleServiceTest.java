package io.mykim.projectboard.article.service;

import io.mykim.projectboard.article.dto.request.ArticleCreateDto;
import io.mykim.projectboard.article.dto.request.ArticleEditDto;
import io.mykim.projectboard.article.dto.request.ArticleSearchCondition;
import io.mykim.projectboard.article.dto.response.ResponseArticleFindDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleListDto;
import io.mykim.projectboard.article.entity.Article;
import io.mykim.projectboard.article.entity.Hashtag;
import io.mykim.projectboard.article.enums.SearchType;
import io.mykim.projectboard.article.repository.ArticleRepository;
import io.mykim.projectboard.article.repository.HashtagRepository;
import io.mykim.projectboard.config.WithAuthUser;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.exception.NotFoundException;
import io.mykim.projectboard.user.dto.request.UserCreateDto;
import io.mykim.projectboard.user.entity.User;
import io.mykim.projectboard.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@DisplayName("ArticleService에 정의된 Article 엔티티에 대한 CRUD 비지니스 로직을 테스트한다.")
@Transactional
@SpringBootTest(properties = {"JASYPT_SECRET_KEY=test"})
class ArticleServiceTest {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("새로운 Article 생성하고 저장한다.")
    @WithAuthUser(username = "test")
    void createArticleTest() throws Exception {
        // given
        String title = "title";
        String content = "content";

        String hashtag1 = "#hashtag";
        String hashtag2 = "#red";
        String hashtag3 = "#blue";

        String hashtags = hashtag1.concat(hashtag2).concat(hashtag3);
        ArticleCreateDto createDto = new ArticleCreateDto(title, content, hashtags);

        // when
        Long articleId = articleService.createArticle(createDto);

        // then
        Article findArticle = articleRepository.findById(articleId).get();
        Assertions.assertThat(title).isEqualTo(findArticle.getTitle());
        Assertions.assertThat(content).isEqualTo(findArticle.getContent());

        List<String> hashtagNames = findArticle.getArticleHashTags()
                                                .stream()
                                                .map(articleHashTag -> articleHashTag.getHashtag().getName())
                                                .collect(Collectors.toList());

        Assertions.assertThat(hashtagNames).contains(hashtag1.replace("#", ""), hashtag2.replace("#", ""), hashtag3.replace("#", ""));
    }

    @Test
    @DisplayName("id로 해당 Article을 조회한다.")
    @WithAuthUser(username = "test")
    void findOneArticleByIdTest() throws Exception {
        // given
        String title = "title";
        String content = "content";

        String hashtag1 = "hashtag";
        String hashtag2 = "red";
        String hashtag3 = "blue";

        Set<Hashtag> hashtags = createHashtags(new String[]{hashtag1, hashtag2, hashtag3});
        Article newArticle = createNewArticle(title, content, hashtags);

        // when
        ResponseArticleFindDto findArticle = articleService.findOneArticle(newArticle.getId());


        // then
        Assertions.assertThat(findArticle).isNotNull();
        Assertions.assertThat(findArticle.getTitle()).isEqualTo(title);
        Assertions.assertThat(findArticle.getContent()).isEqualTo(content);
        Assertions.assertThat(findArticle.getHashtags()).contains(hashtag1, hashtag2, hashtag3);
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

        String hashtag1 = "hashtag";
        String hashtag2 = "red";
        String hashtag3 = "blue";
        Set<Hashtag> hashtags = createHashtags(new String[]{hashtag1, hashtag2, hashtag3});
        IntStream.range(1, 21)
                .forEach(i -> createNewArticle("title"+i, "content"+i, hashtags));

        int page = 1;   // 0부터 시작
        int size = 5;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));

        String searchKeyword = "";
        SearchType searchType = SearchType.ALL;

        // when
        ResponseArticleListDto result = articleService.findAllArticle(searchKeyword, searchType, pageRequest);

        // then
        Assertions.assertThat(result.getResponseArticleFindDtos().size()).isEqualTo(size);
        Assertions.assertThat(result.getResponseArticleFindDtos().get(0).getTitle()).isEqualTo("title6");
        Assertions.assertThat(result.getResponseArticleFindDtos().get(0).getHashtags()).contains(hashtag1, hashtag2, hashtag3);
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
        ArticleCreateDto articleCreateDto = new ArticleCreateDto(title, content);
        Article article = Article.createArticle(articleCreateDto, new LinkedHashSet<>());
        articleRepository.save(article);
        return article;
    }

    private Set<Hashtag> createHashtags(String... hashtagNames) {
        Set<Hashtag> hashtags = Arrays.stream(hashtagNames)
                                        .map(hm -> Hashtag.of(hm))
                                        .collect(Collectors.toUnmodifiableSet());
        hashtagRepository.saveAll(hashtags);
        return hashtags;
    }

    private Article createNewArticle(String title, String content, Set<Hashtag> hashtags) {
        ArticleCreateDto articleCreateDto = new ArticleCreateDto(title, content);
        Article article = Article.createArticle(articleCreateDto, hashtags);
        articleRepository.save(article);
        return article;
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