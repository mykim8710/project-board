package io.mykim.projectboard.article.controller;

import io.mykim.projectboard.article.dto.response.ResponseArticleFindDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleForEditDto;
import io.mykim.projectboard.article.dto.response.ResponseArticleListDto;
import io.mykim.projectboard.article.enums.SearchType;
import io.mykim.projectboard.article.service.ArticleService;
import io.mykim.projectboard.config.WithAuthUser;
import io.mykim.projectboard.global.config.security.SpringSecurityConfig;
import io.mykim.projectboard.global.pageable.CustomPaginationResponse;
import io.mykim.projectboard.global.pageable.PageableRequestCondition;
import io.mykim.projectboard.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.TEXT_HTML;

@DisplayName("ArticleViewController 테스트 - 게시글 View")
@Import({SpringSecurityConfig.class})
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest(ArticleViewController.class)
// Application Context 완전하게 Start 시키지 않고 web layer를 테스트 하고 싶을 때
class ArticleViewControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ArticleService articleService;
    @MockBean
    private UserRepository userRepository;

    @Test
    @DisplayName("[VIEW] [GET] 게시글 목록 페이지 - 정상호출")
    void articlesViewTest() throws Exception{
        // given
        given(articleService.findAllArticle(any(), any(), any()))
                .willReturn(
                        ResponseArticleListDto.builder()
                                                    .responseArticleFindDtos(List.of())
                                                    .paginationResponse(CustomPaginationResponse.of(1,2,3))
                                                    .pageableRequestCondition(PageableRequestCondition
                                                                                    .builder()
                                                                                    .page(1)
                                                                                    .size(1)
                                                                                    .searchKeyword("")
                                                                                    .searchType(SearchType.ALL)
                                                                                    .sort(Sort.by(Sort.Direction.DESC, "id"))
                                                                                    .build())
                                                    .build()
                );

        String url = "/articles";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/list"))  // viewName 확인
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles"))   // model에 해당 key값이 있는지
                .andExpect(MockMvcResultMatchers.model().attributeExists("articleSearchTypes"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("hashtagSearchType"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[VIEW] [GET] 게시글 해시태그 전용 검색 페이지 - 정상호출")
    void articlesHashtagSearchViewTest() throws Exception{
        // given
        given(articleService.findAllArticle(any(), any(), any()))
                .willReturn(
                        ResponseArticleListDto.builder()
                                .responseArticleFindDtos(List.of())
                                .paginationResponse(CustomPaginationResponse.of(1,2,3))
                                .pageableRequestCondition(PageableRequestCondition
                                        .builder()
                                        .page(1)
                                        .size(1)
                                        .searchKeyword("")
                                        .searchType(SearchType.ALL)
                                        .sort(Sort.by(Sort.Direction.DESC, "id"))
                                        .build())
                                .build()
                );

        String url = "/articles/hashtag";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/search-hashtag"))  // viewName 확인
                .andExpect(MockMvcResultMatchers.model().attributeExists("articles"))             // model에 해당 key값이 있는지
                .andExpect(MockMvcResultMatchers.model().attributeExists("hashtagSearchType"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[VIEW] [GET] 게시글 상세 페이지 - 정상호출")
    void articleDetailViewTest() throws Exception{
        // given
        given(articleService.findOneArticle(1L))
                .willReturn(new ResponseArticleFindDto(1L,
                                                            "title",
                                                            "content",
                                                            LocalDateTime.now(),
                                                            LocalDateTime.now(),
                                                            1L,
                                                    "mykim",
                                                    "hashtags"));

        String url = "/articles/{articleId}";
        Long articleId = 1L;

        // when & then

        mockMvc.perform(MockMvcRequestBuilders.get(url, articleId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/detail"))    // viewName 확인
                .andExpect(MockMvcResultMatchers.model().attributeExists("article"))        // model에 해당 key값이 있는지
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[VIEW] [GET] 게시글 작성 페이지 - 정상호출")
    @WithAuthUser(username = "test")
    void articleCreateViewTest() throws Exception{
        // given
        String url = "/articles/create";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/create"))    // viewName 확인
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[VIEW] [GET] 게시글 수정 페이지 - 정상호출")
    @WithAuthUser(username = "test")
    void articleEditViewTest() throws Exception{
        // given
        given(articleService.findOneArticleForEdit(1L))
                .willReturn(new ResponseArticleForEditDto(1L,
                                                        "title",
                                                        "content",
                                                        1L,
                                                        "#hashtag"));

        String url = "/articles/{articleId}/edit";
        Long articleId = 1L;

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(url, articleId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("articles/edit"))    // viewName 확인
                .andExpect(MockMvcResultMatchers.model().attributeExists("article"))      // model에 해당 key값이 있는지
                .andDo(MockMvcResultHandlers.print());
    }
}