package io.mykim.projectboard.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mykim.projectboard.domain.entity.Article;
import io.mykim.projectboard.dto.request.ArticleCreateDto;
import io.mykim.projectboard.dto.request.ArticleEditDto;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.enums.CustomSuccessCode;
import io.mykim.projectboard.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("ArticleApiController에 정의한 v1 api를 테스트한다.")
@Slf4j
@Transactional
@AutoConfigureMockMvc // @SpringBootTest MockMvc 객체사용
@SpringBootTest
class ArticleApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("[v1 / 인증 X] 새로운 게시글을 저장하는 api를 호출하면 게시글이 저장된다.")
    void createArticleApiTest() throws Exception {
        // given
        String api = "/api/v1/articles";

        String title = "aa";
        String content = "cc";
        String hashtag = "#gg";
        ArticleCreateDto createDto = new ArticleCreateDto(title, content, hashtag);
        String requestDtoJsonStr = objectMapper.writeValueAsString(createDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(api)
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
    @DisplayName("[v1 / 인증 X] 게시글 단건 조회 api 요청하면 조건에 맞는 게시글이 응답된다.")
    void findOneArticleApiTest() throws Exception {
        // given
        String title = "aa";
        String content = "cc";
        String hashtag = "#gg";
        Article article = createNewArticle(title, content, hashtag);
        String api = "/api/v1/articles/{articleId}";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(api, article.getId())
                            .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.articleTitle").value(title))
                .andExpect(jsonPath("$.data.articleContent").value(content))
                .andExpect(jsonPath("$.data.articleHashtag").value(hashtag))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[v1 / 인증 X] 존재하지않는 게시글에 대해 단건 조회 api 요청하면 NotFoundException 예외가 발생한다.")
    void findOneArticleApiExceptionTest() throws Exception {
        // given
        String api = "/api/v1/articles/{articleId}";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(api, -1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.status").value(CustomErrorCode.NOT_FOUND_ARTICLE.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomErrorCode.NOT_FOUND_ARTICLE.getCode()))
                .andExpect(jsonPath("$.message").value(CustomErrorCode.NOT_FOUND_ARTICLE.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[v1 / 인증 X] 게시글 전체 조회 api 요청하면 조건에 맞는 게시글 목록이 응답된다.")
    void findAllArticleApiTest() throws Exception {
        // given
        List<Article> articles = IntStream.range(1, 31)
                .mapToObj(i -> Article.of("title" + i, "content" + i, "#hashtag" + i))
                .collect(Collectors.toList());
        articleRepository.saveAll(articles);

        String api = "/api/v1/articles";
        String sort = "id,DESC";
        String keyword = "";
        String searchType = "";
        int offset = 1;
        int limit = 5;

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(api)
                        .queryParam("offset", String.valueOf(offset))
                        .queryParam("limit", String.valueOf(limit))
                        .queryParam("sort", sort)
                        .queryParam("keyword", keyword)
                        .queryParam("searchType", searchType))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(CustomSuccessCode.COMMON_OK.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomSuccessCode.COMMON_OK.getCode()))
                .andExpect(jsonPath("$.message").value(CustomSuccessCode.COMMON_OK.getMessage()))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data.responseArticleFindDtos.length()", Matchers.is(limit)))
                .andExpect(jsonPath("$.data.responseArticleFindDtos[0].articleTitle").value("title30"))
                .andExpect(jsonPath("$.data.responseArticleFindDtos[1].articleTitle").value("title29"))
                .andExpect(jsonPath("$.data.responseArticleFindDtos[2].articleTitle").value("title28"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[v1 / 인증 X] 게시글 수정 api를 요청하면 해당 게시글이 수정된다.")
    void editArticleApiTest() throws Exception {
        // given
        String api = "/api/v1/articles/{articleId}";

        String title = "aa";
        String content = "cc";
        String hashtag = "#gg";
        Article insertArticle = createNewArticle(title, content, hashtag);

        String editTitle = "qwerty";
        String editContent = "qwerty";
        String editHashtag = "#qwerty";
        ArticleEditDto editDto = new ArticleEditDto(editTitle, editContent, editHashtag);
        String requestDtoJsonStr = objectMapper.writeValueAsString(editDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.patch(api, insertArticle.getId())
                .contentType(APPLICATION_JSON)
                .content(requestDtoJsonStr))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(CustomSuccessCode.UPDATE_OK.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomSuccessCode.UPDATE_OK.getCode()))
                .andExpect(jsonPath("$.message").value(CustomSuccessCode.UPDATE_OK.getMessage()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[v1 / 인증 X] 존재하지않는 게시글에 대해 게시글 수정 api를 요청하면 NotFoundException 예외가 발생한다.")
    void editArticleApiExceptionTest() throws Exception {
        // given
        String api = "/api/v1/articles/{articleId}";

        String title = "aa";
        String content = "cc";
        String hashtag = "#gg";
        Article insertArticle = createNewArticle(title, content, hashtag);

        String editTitle = "qwerty";
        String editContent = "qwerty";
        String editHashtag = "#qwerty";
        ArticleEditDto editDto = new ArticleEditDto(editTitle, editContent, editHashtag);
        String requestDtoJsonStr = objectMapper.writeValueAsString(editDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.patch(api, -1L)
                        .contentType(APPLICATION_JSON)
                        .content(requestDtoJsonStr))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.status").value(CustomErrorCode.NOT_FOUND_ARTICLE.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomErrorCode.NOT_FOUND_ARTICLE.getCode()))
                .andExpect(jsonPath("$.message").value(CustomErrorCode.NOT_FOUND_ARTICLE.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[v1 / 인증 X] 게시글 삭제 api를 요청하면 해당 게시글이 삭제된다.")
    void removeArticleApiTest() throws Exception {
        // given
        String api = "/api/v1/articles/{articleId}";

        String title = "aa";
        String content = "cc";
        String hashtag = "#gg";
        Article insertArticle = createNewArticle(title, content, hashtag);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete(api, insertArticle.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(CustomSuccessCode.DELETE_OK.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomSuccessCode.DELETE_OK.getCode()))
                .andExpect(jsonPath("$.message").value(CustomSuccessCode.DELETE_OK.getMessage()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[v1 / 인증 X] 존재하지않는 게시글에 대해 게시글 삭제 api를 요청하면 NotFoundException 예외가 발생한다.")
    void removeArticleApiExceptionTest() throws Exception {
        // given
        String api = "/api/v1/articles/{articleId}";

        String title = "aa";
        String content = "cc";
        String hashtag = "#gg";
        Article insertArticle = createNewArticle(title, content, hashtag);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete(api, -1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.status").value(CustomErrorCode.NOT_FOUND_ARTICLE.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomErrorCode.NOT_FOUND_ARTICLE.getCode()))
                .andExpect(jsonPath("$.message").value(CustomErrorCode.NOT_FOUND_ARTICLE.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    private Article createNewArticle(String title, String content, String hashtag) {
        Article article = Article.of(title, content, hashtag);
        articleRepository.save(article);
        return article;
    }

}