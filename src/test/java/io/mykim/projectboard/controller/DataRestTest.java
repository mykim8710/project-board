package io.mykim.projectboard.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@Disabled("data rest가 적용한 api사용 안할예정이므로 테스트 진행안함")
@DisplayName("Data REST, HAL로 설정한 api 테스트")
@Slf4j
@AutoConfigureMockMvc // @SpringBootTest에서 MockMvc 객체를 사용하기 위함
@SpringBootTest
@Transactional  // 통합테스트
public class DataRestTest {
    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("get api 테스트 - 게시글 리스트 조회")
    void findArticlesApiTest() throws Exception{
        // given
        String api = "/api/articles";

        // when & then
        mvc.perform(MockMvcRequestBuilders.get(api))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content().contentType((MediaType.valueOf("application/hal+json"))))
                    .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("get api 테스트 - 게시글 단건 조회")
    void findArticleByIdApiTest() throws Exception{
        // given
        String api = "/api/articles/{articleId}";

        // when & then
        mvc.perform(MockMvcRequestBuilders.get(api, 2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType((MediaType.valueOf("application/hal+json"))))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("get api 테스트 - 게시글 -> 댓글 리스트 조회")
    void findArticleCommentsInArticleApiTest() throws Exception{
        // given
        String api = "/api/articles/{articleId}/articleComments";

        // when & then
        mvc.perform(MockMvcRequestBuilders.get(api, 2))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType((MediaType.valueOf("application/hal+json"))))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("get api 테스트 - 전체 댓글 리스트 조회")
    void findArticleCommentsApiTest() throws Exception{
        // given
        String api = "/api/articleComments";

        // when & then
        mvc.perform(MockMvcRequestBuilders.get(api))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType((MediaType.valueOf("application/hal+json"))))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("get api 테스트 - 댓글 단건 조회")
    void findArticleCommentByIdApiTest() throws Exception{
        // given
        String api = "/api/articleComments/{articleCommentId}";

        // when & then
        mvc.perform(MockMvcRequestBuilders.get(api, 100))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType((MediaType.valueOf("application/hal+json"))))
                .andDo(MockMvcResultHandlers.print());
    }
}
