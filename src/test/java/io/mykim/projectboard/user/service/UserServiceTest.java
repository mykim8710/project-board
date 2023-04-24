package io.mykim.projectboard.user.service;

import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.exception.NotValidRequestException;
import io.mykim.projectboard.user.dto.request.UserCreateDto;
import io.mykim.projectboard.user.dto.request.UserInfoDuplicateCheckDto;
import io.mykim.projectboard.user.entity.User;
import io.mykim.projectboard.global.result.exception.DuplicateUserInfoException;
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

@DisplayName("UserService에 정의된 User 엔티티에 대한 CRUD 비지니스 로직을 테스트한다.")
@Transactional
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("새로운 유저를 생성하고 저장한다. : 회원가입")
    void createUserTest() throws Exception {
        // given
        String username = "test";
        String password = "1234";
        String email = "email";
        String nickname = "nickname";
        String memo = "memo";

        UserCreateDto createDto = UserCreateDto.builder()
                                                    .username(username)
                                                    .password(password)
                                                    .email(email)
                                                    .nickname(nickname)
                                                    .memo(memo)
                                                    .build();

        // when
        Long createdUserId = userService.createUser(createDto);

        // then
        User findUser = entityManager.find(User.class, createdUserId);
        Assertions.assertThat(findUser.getUsername()).isEqualTo(username);
        Assertions.assertThat(findUser.getEmail()).isEqualTo(email);
        Assertions.assertThat(findUser.getNickname()).isEqualTo(nickname);
        Assertions.assertThat(findUser.getMemo()).isEqualTo(memo);
    }

    @DisplayName("회원가입 시 유저정보가 중복되면 DuplicateUserInfoException 예외가 발생한다.")
    @ParameterizedTest(name = "{index} {displayName}, source = {0} {1}")
    @CsvSource({"'username', 'test'", "'nickname', 'nickname'", "'email', 'email@email.com'", "'abc', 'abc'"})
    void duplicateCheckUserInfoTest(ArgumentsAccessor argumentsAccessor) throws Exception {
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

        UserInfoDuplicateCheckDto duplicateCheckDto = UserInfoDuplicateCheckDto.builder()
                                                                                    .type(argumentsAccessor.getString(0))
                                                                                    .keyword(argumentsAccessor.getString(1))
                                                                                    .build();

        // when & then
        Exception e;
        String message;
        switch (argumentsAccessor.getString(0)) {
            case "username":
                e = new DuplicateUserInfoException(CustomErrorCode.DUPLICATE_USER_NAME);
                message = CustomErrorCode.DUPLICATE_USER_NAME.getMessage();
                break;
            case "nickname":
                e = new DuplicateUserInfoException(CustomErrorCode.DUPLICATE_USER_NICKNAME);
                message = CustomErrorCode.DUPLICATE_USER_NICKNAME.getMessage();
                break;
            case "email":
                e = new DuplicateUserInfoException(CustomErrorCode.DUPLICATE_USER_EMAIL);
                message = CustomErrorCode.DUPLICATE_USER_EMAIL.getMessage();
                break;
            default:
                e = new NotValidRequestException(CustomErrorCode.NOT_VALID_REQUEST);
                message = CustomErrorCode.NOT_VALID_REQUEST.getMessage();
                break;
        }

        Assertions.assertThatThrownBy(() -> userService.duplicateCheckUserInfo(duplicateCheckDto))
                .isInstanceOf(e.getClass())
                .hasMessage(message);
    }

    private User createUser(UserCreateDto createDto) {
        User user = User.of(createDto);
        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();
        return user;
    }
}
