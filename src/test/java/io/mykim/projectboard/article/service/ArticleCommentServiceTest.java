package io.mykim.projectboard.article.service;

import io.mykim.projectboard.article.dto.request.ArticleCommentCreateDto;
import io.mykim.projectboard.article.dto.request.ArticleCommentEditDto;
import io.mykim.projectboard.article.dto.request.ArticleCreateDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleCommentFindDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleCommentListDto;
import io.mykim.projectboard.article.entity.Article;
import io.mykim.projectboard.article.entity.ArticleComment;
import io.mykim.projectboard.article.entity.Hashtag;
import io.mykim.projectboard.article.repository.ArticleCommentRepository;
import io.mykim.projectboard.article.repository.ArticleRepository;
import io.mykim.projectboard.config.WithAuthUser;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.exception.NotFoundException;
import io.mykim.projectboard.user.dto.request.UserCreateDto;
import io.mykim.projectboard.user.entity.User;
import io.mykim.projectboard.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@DisplayName("ArticleCommentService에 정의된 ArticleComment 엔티티에 대한 CRUD 비지니스 로직을 테스트한다.")
@Transactional
@SpringBootTest(properties = {"JASYPT_SECRET_KEY=test"})
class ArticleCommentServiceTest {
    @Autowired
    private ArticleCommentService articleCommentService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleCommentRepository articleCommentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("새로운 ArticleComment를 생성하고 저장한다.")
    @WithAuthUser(username = "test")
    void createArticleCommentTest() throws Exception {
        // given
        String title = "title";
        String content = "content";
        Article article = createNewArticle(title, content);

        String commentContent = "content";
        ArticleCommentCreateDto createDto = new ArticleCommentCreateDto(commentContent);

        // when
        Long newArticleCommentId = articleCommentService.createNewArticleComment(createDto, article.getId());

        // then
        ArticleComment findArticleComment = em.find(ArticleComment.class, newArticleCommentId);
        Assertions.assertThat(findArticleComment.getContent()).isEqualTo(commentContent);
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 대해 새로운 ArticleComment를 생성하고 저장할때 NotFoundException(게시글) 예외가 발생한다.")
    void createArticleCommentExceptionTest() throws Exception {
        // given
        Long notFoundArticleId = 1L;

        String commentContent = "content";
        ArticleCommentCreateDto createDto = new ArticleCommentCreateDto(commentContent);

        // when & then
        Assertions.assertThatThrownBy(() ->{
            articleCommentService.createNewArticleComment(createDto, notFoundArticleId);
        }).isInstanceOf(NotFoundException.class)
                .hasMessage(CustomErrorCode.NOT_FOUND_ARTICLE.getMessage());
    }

    @Test
    @DisplayName("해당 id의 ArticleComment를 수정한다.")
    @WithAuthUser(username = "test")
    void editArticleCommentTest() throws Exception {
        // given
        String title = "title";
        String content = "content";
        String hashtag = "hashtag";
        Article article = createNewArticle(title, content);

        String commentContent = "reply~~reply";
        ArticleComment articleComment = createNewArticleComment(article, commentContent);

        String editCommentContent = "reply~~reply~~edit~~edit";
        ArticleCommentEditDto editDto = new ArticleCommentEditDto(editCommentContent);

        // when
        articleCommentService.editArticleComment(editDto, article.getId(), articleComment.getId());

        // then
        ArticleComment findArticleComment = em.find(ArticleComment.class, articleComment.getId());
        Assertions.assertThat(findArticleComment.getContent()).isEqualTo(editCommentContent);
    }

    @DisplayName("존재하지 않는 게시글 id, 댓글 id에 대해 ArticleComment를 수정하려 할때 NotFoundException(댓글) 예외가 발생한다.")
    @ParameterizedTest(name = "{index}, {displayName}, source = {0}, {1}")
    @CsvSource({"-1, -1", "-1, 10", "10, -1"})
    void editArticleCommentExceptionTest(ArgumentsAccessor argumentsAccessor) throws Exception {
        // given
        String editCommentContent = "reply~~reply~~edit~~edit";
        ArticleCommentEditDto editDto = new ArticleCommentEditDto(editCommentContent);

        // when & then
        Assertions.assertThatThrownBy(() -> {
            articleCommentService.editArticleComment(editDto, argumentsAccessor.getLong(0), -argumentsAccessor.getLong(1));
        }).isInstanceOf(NotFoundException.class)
                .hasMessage(CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT.getMessage());
    }

    @Test
    @DisplayName("해당 id의 ArticleComment를 삭제한다.")
    @WithAuthUser(username = "test")
    void removeArticleCommentTest() throws Exception {
        // given
        String title = "title";
        String content = "content";
        Article article = createNewArticle(title, content);

        String commentContent = "reply~~reply";
        ArticleComment articleComment = createNewArticleComment(article, commentContent);

        // when
        articleCommentService.removeArticleComment(article.getId(), articleComment.getId());

        // then
        Assertions.assertThatThrownBy(() -> {
            articleCommentRepository.findById(articleComment.getId()).get();
        }).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("존재하지 않는 게시글 id, 댓글 id에 대해 ArticleComment를 삭제하려 할때 NotFoundException(댓글) 예외가 발생한다.")
    @ParameterizedTest(name = "{index}, {displayName}, source = {0}, {1}")
    @CsvSource({"-1, -1", "-1, 10", "10, -1"})
    void removeArticleCommentExceptionTest(ArgumentsAccessor argumentsAccessor) throws Exception {
        // given

        // when & then
        Assertions.assertThatThrownBy(() -> {
                    articleCommentService.removeArticleComment(argumentsAccessor.getLong(0), -argumentsAccessor.getLong(1));
                }).isInstanceOf(NotFoundException.class)
                .hasMessage(CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT.getMessage());
    }

    @Test
    @DisplayName("게시글 하부에 속한 댓글 단건에 대해 조회한다.")
    @WithAuthUser(username = "test")
    void findOneArticleCommentByIdUnderArticleTest() throws Exception {
        // given
        String title = "title";
        String content = "content";
        Article article = createNewArticle(title, content);

        String commentContent = "content";
        ArticleCommentCreateDto createDto = new ArticleCommentCreateDto(commentContent);
        Long newArticleCommentId = articleCommentService.createNewArticleComment(createDto, article.getId());

        // when
        ResponseArticleCommentFindDto findArticleCommentDto = articleCommentService.findOneArticleCommentByIdUnderArticle(article.getId(), newArticleCommentId);

        // then
        Assertions.assertThat(findArticleCommentDto.getArticleCommentContent()).isEqualTo(commentContent);
    }

    @Test
    @DisplayName("게시글 하부에 속한 댓글 단건에 대해 조회 시 존재하지 않는 댓글이면 NotFoundException(댓글)이 발생한다.")
    void findOneArticleCommentByIdUnderArticleExceptionTest() throws Exception {
        // given
        Long notFoundArticleId = -1L;
        Long notFoundArticleCommentId = -1L;

        // when & then
        Assertions.assertThatThrownBy(() -> articleCommentService.findOneArticleCommentByIdUnderArticle(notFoundArticleId, notFoundArticleCommentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT.getMessage());
    }

    @Test
    @DisplayName("게시글 하부 댓글에 대해 조건에 맞는 리스트를 불러온다.")
    @WithAuthUser(username = "test")
    void findAllArticleCommentUnderArticleTest() throws Exception {
        // given
        createUser();
        Article article = createNewArticle("title", "content");
        IntStream.range(1, 31)
                .forEach(i -> createNewArticleComment(article, "reply_" + i));


        int offset = 1;
        int limit = 5;
//        CustomPaginationRequest customPaginationRequest = new CustomPaginationRequest(offset, limit);
//
//        // when
//        ResponseArticleCommentListDto result = articleCommentService.findAllArticleCommentUnderArticle(customPaginationRequest, article.getId());
//
//        // then
//        Assertions.assertThat(result.getResponseArticleCommentFindDtos().size()).isEqualTo(limit);
//        Assertions.assertThat(result.getResponseArticleCommentFindDtos().get(0).getArticleCommentContent()).isEqualTo("reply_30");
    }


    private ArticleComment createNewArticleComment(Article article, String commentContent) {
        ArticleComment articleComment = ArticleComment.of(commentContent, article);
        return articleCommentRepository.save(articleComment);
    }

    private Article createNewArticle(String title, String content) {
        ArticleCreateDto articleCreateDto = new ArticleCreateDto(title, content);
        Set<Hashtag> hashtags = IntStream.range(1, 10)
                .mapToObj(i-> Hashtag.of("blue_"+i))
                .collect(Collectors.toUnmodifiableSet());

        Article article =  Article.createArticle(articleCreateDto, hashtags);
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