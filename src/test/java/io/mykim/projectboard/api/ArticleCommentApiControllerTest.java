package io.mykim.projectboard.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mykim.projectboard.domain.entity.Article;
import io.mykim.projectboard.domain.entity.ArticleComment;
import io.mykim.projectboard.dto.request.ArticleCommentCreateDto;
import io.mykim.projectboard.dto.request.ArticleCreateDto;
import io.mykim.projectboard.global.result.enums.CustomSuccessCode;
import io.mykim.projectboard.repository.ArticleCommentRepository;
import io.mykim.projectboard.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("ArticleCommentApiController에 정의한 v1 api를 테스트한다.")
@Slf4j
@Transactional
@AutoConfigureMockMvc // @SpringBootTest MockMvc 객체사용
@SpringBootTest
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
    @DisplayName("[v1 / 인증 X] 새로운 게시글을 저장하는 api를 호출하면 게시글이 저장된다.")
    void createArticleApiTest() throws Exception {
        // given
        Article article = createNewArticle("title", "content", "##");

        String api = "/api/v1/articles/{articleId}/article-comments";
        String content = "cc";
        ArticleCommentCreateDto createDto = new ArticleCommentCreateDto(content);
        String requestDtoJsonStr = objectMapper.writeValueAsString(createDto);

        // when
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


        // then
        long count = articleCommentRepository.count();
        Assertions.assertThat(count).isEqualTo(1);

        ArticleComment findArticleComment = articleCommentRepository.findAll().get(0);
        Assertions.assertThat(findArticleComment.getContent()).isEqualTo(content);
        Assertions.assertThat(findArticleComment.getArticle().getTitle()).isEqualTo(article.getTitle());
    }


    private Article createNewArticle(String title, String content, String hashtag) {
        Article article = Article.of(title, content, hashtag);
        articleRepository.save(article);
        return article;
    }
}