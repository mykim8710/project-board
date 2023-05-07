package io.mykim.projectboard.article.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mykim.projectboard.article.dto.request.ArticleCommentCreateDto;
import io.mykim.projectboard.article.dto.request.ArticleCommentEditDto;
import io.mykim.projectboard.article.dto.request.ArticleCreateDto;
import io.mykim.projectboard.article.entity.Article;
import io.mykim.projectboard.article.entity.ArticleComment;
import io.mykim.projectboard.article.entity.Hashtag;
import io.mykim.projectboard.article.repository.ArticleCommentRepository;
import io.mykim.projectboard.article.repository.ArticleRepository;
import io.mykim.projectboard.config.WithAuthUser;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.enums.CustomSuccessCode;
import io.mykim.projectboard.user.dto.request.UserCreateDto;
import io.mykim.projectboard.user.entity.User;
import io.mykim.projectboard.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("ArticleCommentApiController에 정의한 v1 api를 테스트한다.")
@Slf4j
@Transactional
@AutoConfigureMockMvc // @SpringBootTest MockMvc 객체사용
@SpringBootTest(properties = {"JASYPT_SECRET_KEY=test"})
class ArticleCommentApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleCommentRepository articleCommentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("[v1 / 인증 적용] 게시글 하부에 새로운 뎃글을 저장하는 api를 호출하면 댓글이 저장된다.")
    @WithAuthUser(username = "test")
    void createArticleCommentApiTest() throws Exception {
        // given
        Article article = createNewArticle("title", "content");

        String api = "/api/v1/articles/{articleId}/article-comments";
        String content = "cc";
        ArticleCommentCreateDto createDto = new ArticleCommentCreateDto(null, content);
        String requestDtoJsonStr = objectMapper.writeValueAsString(createDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(api, article.getId())
                        .contentType(APPLICATION_JSON)
                        .content(requestDtoJsonStr)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(CustomSuccessCode.INSERT_OK.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomSuccessCode.INSERT_OK.getCode()))
                .andExpect(jsonPath("$.message").value(CustomSuccessCode.INSERT_OK.getMessage()))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[v1 / 인증 적용] 존재하지 않는 게시글 하부에 새로운 뎃글을 저장하는 api를 호출하면 NotFoundException(게시글) 예외가 발생한다.")
    @WithAuthUser(username = "test")
    void createArticleCommentApiExceptionTest() throws Exception {
        // given
        Long notFoundArticleId = -1L;

        String api = "/api/v1/articles/{articleId}/article-comments";
        String content = "cc";
        ArticleCommentCreateDto createDto = new ArticleCommentCreateDto(null,content);
        String requestDtoJsonStr = objectMapper.writeValueAsString(createDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(api, notFoundArticleId)
                        .contentType(APPLICATION_JSON)
                        .content(requestDtoJsonStr)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.status").value(CustomErrorCode.NOT_FOUND_ARTICLE.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomErrorCode.NOT_FOUND_ARTICLE.getCode()))
                .andExpect(jsonPath("$.message").value(CustomErrorCode.NOT_FOUND_ARTICLE.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[v1 / 인증 적용] 게시글 하부에 뎃글을 수정하는 api를 호출하면 댓글이 수정된다.")
    @WithAuthUser(username = "test")
    void editArticleCommentApiTest() throws Exception {
        // given
        Article article = createNewArticle("title", "content");
        ArticleComment articleComment = createNewArticleComment(article, "reply");

        String api = "/api/v1/articles/{articleId}/article-comments/{articleCommentId}";
        String editCommentContent = "dddd";
        ArticleCommentEditDto editDto = new ArticleCommentEditDto(editCommentContent);
        String requestDtoJsonStr = objectMapper.writeValueAsString(editDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.patch(api, article.getId(), articleComment.getId())
                        .contentType(APPLICATION_JSON)
                        .content(requestDtoJsonStr)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(CustomSuccessCode.UPDATE_OK.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomSuccessCode.UPDATE_OK.getCode()))
                .andExpect(jsonPath("$.message").value(CustomSuccessCode.UPDATE_OK.getMessage()))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("[v1 / 인증 적용] 존재하지 않는 게시글 하부 댓글에 대해 수정하는 api를 호출하면 NotFoundException(댓글) 예외가 발생한다.")
    @ParameterizedTest(name = "{index}, {displayName}, source = {0}, {1}")
    @CsvSource({"-1, -1", "-1, 10", "10, -1"})
    @WithAuthUser(username = "test")
    void editArticleCommentApiExceptionTest(ArgumentsAccessor argumentsAccessor) throws Exception {
        // given
        String api = "/api/v1/articles/{articleId}/article-comments/{articleCommentId}";
        String editCommentContent = "dddd";
        ArticleCommentEditDto editDto = new ArticleCommentEditDto(editCommentContent);
        String requestDtoJsonStr = objectMapper.writeValueAsString(editDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.patch(api, argumentsAccessor.getLong(0), argumentsAccessor.getLong(1))
                        .contentType(APPLICATION_JSON)
                        .content(requestDtoJsonStr)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.status").value(CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT.getCode()))
                .andExpect(jsonPath("$.message").value(CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[v1 / 인증 적용] 게시글 하부에 댓글을 삭제하는 api를 호출하면 댓글이 삭제된다.")
    @WithAuthUser(username = "test")
    void removeArticleCommentApiTest() throws Exception {
        // given
        Article article = createNewArticle("title", "content");
        ArticleComment articleComment = createNewArticleComment(article, "reply");
        String api = "/api/v1/articles/{articleId}/article-comments/{articleCommentId}";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete(api, article.getId(), articleComment.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(CustomSuccessCode.DELETE_OK.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomSuccessCode.DELETE_OK.getCode()))
                .andExpect(jsonPath("$.message").value(CustomSuccessCode.DELETE_OK.getMessage()))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("[v1 / 인증 적용] 존재하지 않는 게시글 하부 댓글에 대해 삭제하는 api를 호출하면 NotFoundException(댓글) 예외가 발생한다.")
    @ParameterizedTest(name = "{index}, {displayName}, source = {0}, {1}")
    @CsvSource({"-1, -1", "-1, 10", "10, -1"})
    @WithAuthUser(username = "test")
    void removeArticleCommentApiExceptionTest(ArgumentsAccessor argumentsAccessor) throws Exception {
        // given
        String api = "/api/v1/articles/{articleId}/article-comments/{articleCommentId}";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete(api, argumentsAccessor.getLong(0), argumentsAccessor.getLong(1))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.status").value(CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT.getCode()))
                .andExpect(jsonPath("$.message").value(CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시글 하부 댓글에 대해 단건 조회 api를 호출하면 댓글이 응답된다.")
    @WithAuthUser(username = "test")
    void findOneArticleCommentUnderArticleApiTest() throws Exception {
        // given
        Article article = createNewArticle("title", "content");
        String articleCommentContent = "reply";
        ArticleComment articleComment = createNewArticleComment(article, articleCommentContent);
        String api = "/api/v1/articles/{articleId}/article-comments/{articleCommentId}";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(api, article.getId(), articleComment.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.articleCommentContent").value(articleCommentContent))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("게시판 하부 댓글 중 존재하지 않는 댓글에 대해 단건 조회 api를 호출하면 NotFoundException(댓글) 예외가 발생한다.")
    @ParameterizedTest(name = "{index}, {displayName}, source = {0}, {1}")
    @CsvSource({"-1, -1", "-1, 10", "10, -1"})
    @WithAuthUser(username = "test")
    void findOneArticleCommentUnderArticleApiExceptionTest(ArgumentsAccessor argumentsAccessor) throws Exception {
        // given
        String api = "/api/v1/articles/{articleId}/article-comments/{articleCommentId}";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(api, argumentsAccessor.getLong(0), argumentsAccessor.getLong(1))
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.status").value(CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT.getCode()))
                .andExpect(jsonPath("$.message").value(CustomErrorCode.NOT_FOUND_ARTICLE_COMMENT.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("게시글 하부에 해당하는 댓글목록을 조회하는 api를 호출하면 댓글목록이 응답된다.")
    @WithAuthUser(username = "test")
    void findAllArticleCommentUnderArticleApiTest() throws Exception {
        // given
        Article article = createNewArticle("title", "content");

        IntStream.range(1, 31)
                .forEach(i -> createNewArticleComment(article, "reply_" + i));

        String api = "/api/v1/articles/{articleId}/article-comments";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(api, article.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(CustomSuccessCode.COMMON_OK.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomSuccessCode.COMMON_OK.getCode()))
                .andExpect(jsonPath("$.message").value(CustomSuccessCode.COMMON_OK.getMessage()))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.length()", Matchers.is(30)))
                .andExpect(jsonPath("$.data[0].articleCommentContent").value("reply_30"))
                .andExpect(jsonPath("$.data[1].articleCommentContent").value("reply_29"))
                .andExpect(jsonPath("$.data[2].articleCommentContent").value("reply_28"))
                .andDo(MockMvcResultHandlers.print());
    }

    private ArticleComment createNewArticleComment(Article article, String commentContent) {
        ArticleComment articleComment = ArticleComment.of(commentContent, article);
        articleCommentRepository.save(articleComment);
        return articleComment;
    }

    private Article createNewArticle(String title, String content) {
        ArticleCreateDto articleCreateDto = new ArticleCreateDto(title, content);
        Article article = Article.createArticle(articleCreateDto, new LinkedHashSet<>());
        articleRepository.save(article);
        return article;
    }
}