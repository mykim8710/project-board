package io.mykim.projectboard.user.dto.response;

import io.mykim.projectboard.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@ToString
public class UserSignInResponseDto {
    private Long id;
    private String username;
    private String email;
    private String nickname;

    @Builder
    public UserSignInResponseDto(Long id, String username, String email, String nickname) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.nickname = nickname;
    }

    public static UserSignInResponseDto of(User user) {
        return UserSignInResponseDto.builder()
                                    .id(user.getId())
                                    .username(user.getUsername())
                                    .email(user.getEmail())
                                    .nickname(user.getNickname())
                                    .build();
    }
}
