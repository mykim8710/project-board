package io.mykim.projectboard.article.service;

import io.mykim.projectboard.article.dto.request.ArticleCommentCreateDto;
import io.mykim.projectboard.article.dto.request.ArticleCommentEditDto;
import io.mykim.projectboard.article.dto.request.ArticleCreateDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleCommentFindDto;
import io.mykim.projectboard.article.entity.Article;
import io.mykim.projectboard.article.entity.ArticleComment;
import io.mykim.projectboard.article.repository.ArticleCommentRepository;
import io.mykim.projectboard.article.repository.ArticleRepository;
import io.mykim.projectboard.config.WithAuthUser;
import io.mykim.projectboard.global.config.security.dto.PrincipalDetail;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.exception.NotFoundException;
import io.mykim.projectboard.user.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.IntStream;

@DisplayName("ArticleCommentService에 정의된 ArticleComment 엔티티에 대한 CRUD 비지니스 로직을 테스트한다.")
@Transactional
@SpringBootTest
class ArticleCommentServiceTest {
    @Autowired
    private ArticleCommentService articleCommentService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleCommentRepository articleCommentRepository;

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
        ArticleCommentCreateDto createDto = new ArticleCommentCreateDto(null,commentContent);

        // when
        Long newArticleCommentId = articleCommentService.createNewArticleComment(createDto, article.getId(), getSignInUser());

        // then
        ArticleComment findArticleComment = articleCommentRepository.findById(newArticleCommentId).get();
        Assertions.assertThat(findArticleComment.getContent()).isEqualTo(commentContent);
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 대해 새로운 ArticleComment를 생성하고 저장할때 NotFoundException(게시글) 예외가 발생한다.")
    @WithAuthUser(username = "test")
    void createArticleCommentExceptionTest() throws Exception {
        // given
        Long notFoundArticleId = -1L;

        String commentContent = "content";
        ArticleCommentCreateDto createDto = new ArticleCommentCreateDto(null, commentContent);

        // when & then
        Assertions.assertThatThrownBy(() ->{
            articleCommentService.createNewArticleComment(createDto, notFoundArticleId, getSignInUser());
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
        Article article = createNewArticle(title, content);

        String commentContent = "reply~~reply";
        ArticleComment articleComment = createNewArticleComment(article, commentContent);

        String editCommentContent = "reply~~reply~~edit~~edit";
        ArticleCommentEditDto editDto = new ArticleCommentEditDto(editCommentContent);

        // when
        articleCommentService.editArticleComment(editDto, article.getId(), articleComment.getId());
        articleCommentRepository.flush();

        // then
        ArticleComment findArticleComment = articleCommentRepository.findById(articleComment.getId()).get();
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
        articleCommentRepository.flush();

        // then
        Assertions.assertThat(articleCommentRepository.findById(articleComment.getId())).isEmpty();
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
    @DisplayName("부모 댓글을 삭제하면 부모 댓글하부 자식댓글 모두가 삭제된다.")
    @WithAuthUser(username = "test")
    void parentArticleCommentDeleteWithChildCommentTest() throws Exception {
        // given
        String title = "title";
        String content = "content";
        Article article = createNewArticle(title, content);

        String commentContent = "reply~~reply";
        ArticleComment parentArticleComment = createNewArticleComment(article, commentContent);

        String childCommentContent = "child_comment_content";
        ArticleComment childArticleComment1 = createNewArticleCommentWithParentArticleComment(article, childCommentContent, parentArticleComment);
        ArticleComment childArticleComment2 = createNewArticleCommentWithParentArticleComment(article, childCommentContent, parentArticleComment);
        ArticleComment childArticleComment3 = createNewArticleCommentWithParentArticleComment(article, childCommentContent, parentArticleComment);

        // when
        articleCommentService.removeArticleComment(article.getId(), parentArticleComment.getId());
        articleCommentRepository.flush();

        // then
        Assertions.assertThat(articleCommentRepository.findById(childArticleComment1.getId())).isEmpty();
        Assertions.assertThat(articleCommentRepository.findById(childArticleComment2.getId())).isEmpty();
        Assertions.assertThat(articleCommentRepository.findById(childArticleComment3.getId())).isEmpty();
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
        ArticleCommentCreateDto createDto = new ArticleCommentCreateDto(null,commentContent);
        Long newArticleCommentId = articleCommentService.createNewArticleComment(createDto, article.getId(), getSignInUser());

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
        Article article = createNewArticle("title", "content");
        IntStream.range(1, 31)
                .forEach(i -> createNewArticleComment(article, "reply_" + i));

        // when
        List<ResponseArticleCommentFindDto> result = articleCommentService.findAllArticleCommentUnderArticle(article.getId());

        // then
        Assertions.assertThat(result.size()).isEqualTo(30);
    }


    private ArticleComment createNewArticleComment(Article article, String commentContent) {
        ArticleComment articleComment = ArticleComment.createArticleComment(commentContent, article, getSignInUser());
        return articleCommentRepository.save(articleComment);
    }

    private ArticleComment createNewArticleCommentWithParentArticleComment(Article article, String commentContent, ArticleComment parentArticleComment) {
        ArticleComment articleComment = ArticleComment.createArticleComment(commentContent, article, getSignInUser());
        parentArticleComment.addChildArticleComment(articleComment);
        return articleCommentRepository.save(articleComment);
    }

    private Article createNewArticle(String title, String content) {
        ArticleCreateDto articleCreateDto = new ArticleCreateDto(title, content);
        Article article = Article.createArticle(articleCreateDto, new LinkedHashSet<>(), getSignInUser());
        articleRepository.save(article);
        return article;
    }

    private User getSignInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PrincipalDetail principal = (PrincipalDetail)authentication.getPrincipal();
        return principal.getUser();
    }
}