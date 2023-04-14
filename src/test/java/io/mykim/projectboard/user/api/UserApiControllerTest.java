package io.mykim.projectboard.user.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.enums.CustomSuccessCode;
import io.mykim.projectboard.user.dto.request.UserCreateDto;
import io.mykim.projectboard.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@DisplayName("UserApiController에 정의한 v1 api를 테스트한다.")
@Slf4j
@Transactional
@AutoConfigureMockMvc // @SpringBootTest MockMvc 객체사용
@SpringBootTest
class UserApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("[v1 | POST] 회원가입하는 api를 호출하면 회원가입이 완료된다.")
    void createUserApiTest() throws Exception {
        // given
        String username = "username";
        String password = "1234";
        String nickname = "nickname";
        String email = "abc@abc.com";
        String memo = "";

        UserCreateDto createDto = UserCreateDto.builder()
                                                    .username(username)
                                                    .password(password)
                                                    .nickname(nickname)
                                                    .email(email)
                                                    .memo(memo)
                                                    .build();

        String api = "/api/v1/users";

        String createDtoJsonString = objectMapper.writeValueAsString(createDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(api)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createDtoJsonString))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(CustomSuccessCode.INSERT_OK.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomSuccessCode.INSERT_OK.getCode()))
                .andExpect(jsonPath("$.message").value(CustomSuccessCode.INSERT_OK.getMessage()))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("[v1 | POST] 회원가입하는 api를 호출할때 유효성검사에 통과하지 못하면 MethodArgumentNotValidException(@NotBlank) 예외가 발생한다.")
    @ParameterizedTest(name = "{index} {displayName}, source = {0}")
    @ValueSource(strings = {"username", "password", "nickname", "email"})
    void createUserApiNotBlankValidationExceptionTest(String source) throws Exception {
        // given
        String username = "username";
        String password = "password";
        String nickname = "nickname";
        String email = "abc@abc.com";
        String memo = "";

        String errorFieldName = "";
        String errorMessage = "";

        switch (source) {
            case "username":
                username = "  ";
                errorFieldName = "username";
                errorMessage = "아이디를 입력해주세요.";
                break;
            case "password":
                password = "  ";
                errorFieldName = "password";
                errorMessage = "비밀번호를 입력해주세요.";
                break;
            case "nickname":
                nickname = "  ";
                errorFieldName = "nickname";
                errorMessage = "닉네임을 입력해주세요.";
                break;
            case "email":
                email = "";
                errorFieldName = "email";
                errorMessage = "이메일을 입력해주세요.";
                break;
        }

        UserCreateDto createDto = UserCreateDto.builder()
                                                .username(username)
                                                .password(password)
                                                .nickname(nickname)
                                                .email(email)
                                                .memo(memo)
                                                .build();

        String api = "/api/v1/users";

        String createDtoJsonString = objectMapper.writeValueAsString(createDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(api)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createDtoJsonString))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(CustomErrorCode.VALIDATION_ERROR.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomErrorCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(CustomErrorCode.VALIDATION_ERROR.getMessage()))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data[0].fieldName").value(errorFieldName))
                .andExpect(jsonPath("$.data[0].errorMessage").value(errorMessage))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("[v1 | POST] 회원가입하는 api를 호출할때 유효성검사에 통과하지 못하면 MethodArgumentNotValidException(@Email) 예외가 발생한다.")
    @Test
    void createUserApiEmailFormValidationExceptionTest() throws Exception {
        // given
        String username = "username";
        String password = "password";
        String nickname = "nickname";
        String email = "abc";
        String memo = "";

        UserCreateDto createDto = UserCreateDto.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .email(email)
                .memo(memo)
                .build();

        String api = "/api/v1/users";

        String createDtoJsonString = objectMapper.writeValueAsString(createDto);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post(api)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createDtoJsonString))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(CustomErrorCode.VALIDATION_ERROR.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomErrorCode.VALIDATION_ERROR.getCode()))
                .andExpect(jsonPath("$.message").value(CustomErrorCode.VALIDATION_ERROR.getMessage()))
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data[0].fieldName").value("email"))
                .andExpect(jsonPath("$.data[0].errorMessage").value("이메일 형식이 아닙니다."))
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("회원가입 중 중복체크 api를 호출할때 해당 필드가 중복되지않으면 정상 응답을 리턴한다.")
    @ParameterizedTest(name = "{index} {displayName}, source = {0} {1}")
    @CsvSource({"'username', 'test2'", "'nickname', 'nicknam2e'", "'email', 'email2@email.com'"})
    void duplicateCheckUserInfoApiTest(ArgumentsAccessor argumentsAccessor) throws Exception {
        // given
        String username = "test";
        String password = "1234";
        String email = "email@email.com";
        String nickname = "nickname";
        String memo = "memo";

        UserCreateDto createDto = UserCreateDto.builder()
                                                .username(username)
                                                .password(password)
                                                .email(email)
                                                .nickname(nickname)
                                                .memo(memo)
                                                .build();

        createUser(createDto);

        String api = "/api/v1/users/duplicate-check";

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get(api)
                        .queryParam("type", argumentsAccessor.getString(0))
                        .queryParam("keyword", argumentsAccessor.getString(1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.status").value(CustomSuccessCode.COMMON_OK.getStatus()))
                .andExpect(jsonPath("$.code").value(CustomSuccessCode.COMMON_OK.getCode()))
                .andExpect(jsonPath("$.message").value(CustomSuccessCode.COMMON_OK.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("회원가입 중 중복체크 api를 호출할때 해당 필드가 중복된다면 DuplicateUserInfoException 예외가 발생한다.")
    @ParameterizedTest(name = "{index} {displayName}, source = {0} {1}")
    @CsvSource({"'username', 'test'", "'nickname', 'nickname'", "'email', 'email@email.com'", "'abc', 'abc'"})
    void duplicateCheckUserInfoApiExceptionTest(ArgumentsAccessor argumentsAccessor) throws Exception {
        // given
        String username = "test";
        String password = "1234";
        String email = "email@email.com";
        String nickname = "nickname";
        String memo = "memo";

        UserCreateDto createDto = UserCreateDto.builder()
                                                .username(username)
                                                .password(password)
                                                .email(email)
                                                .nickname(nickname)
                                                .memo(memo)
                                                .build();

        createUser(createDto);

        String api = "/api/v1/users/duplicate-check";

        // when & then
        CustomErrorCode ec;
        switch (argumentsAccessor.getString(0)) {
            case "username":
                ec = CustomErrorCode.DUPLICATE_USER_NAME;
                break;
            case "nickname":
                ec = CustomErrorCode.DUPLICATE_USER_NICKNAME;
                break;
            case "email":
                ec = CustomErrorCode.DUPLICATE_USER_EMAIL;
                break;
            default:
                ec = CustomErrorCode.NOT_VALID_REQUEST;
                break;
        }

        mockMvc.perform(MockMvcRequestBuilders.get(api)
                                .queryParam("type", argumentsAccessor.getString(0))
                                .queryParam("keyword", argumentsAccessor.getString(1)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(jsonPath("$.status").value(ec.getStatus()))
                .andExpect(jsonPath("$.code").value(ec.getCode()))
                .andExpect(jsonPath("$.message").value(ec.getMessage()))
                .andExpect(jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    private User createUser(UserCreateDto createDto) {
        User user = User.of(createDto);
        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();
        return user;
    }

}
