package io.mykim.projectboard.user.controller;

import io.mykim.projectboard.global.config.security.SpringSecurityConfig;
import io.mykim.projectboard.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.MediaType.TEXT_HTML;

@DisplayName("UserViewController 테스트 - 사용자 View")
@Import(SpringSecurityConfig.class)
@WebMvcTest(UserViewController.class)
class UserViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("[VIEW] [GET] 회원가입 페이지 - 정상호출")
    void createUserViewCode() throws Exception {
        // given
        String url = "/users/sign-up";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("users/sign-up")) // viewName 확인
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))      // model에 해당 key값이 있는지
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("[VIEW] [GET] 로그인 페이지 - 정상호출")
    void authenticateUserViewCode() throws Exception {
        // given
        String url = "/users/sign-in";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(TEXT_HTML))
                .andExpect(MockMvcResultMatchers.view().name("users/sign-in")) // viewName 확인
                .andDo(MockMvcResultHandlers.print());
    }

}