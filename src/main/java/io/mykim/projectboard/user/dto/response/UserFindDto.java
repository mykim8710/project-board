package io.mykim.projectboard.user.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class UserFindDto {
    private Long id;
    private String username;
    private String nickname;
    private String email;

    @QueryProjection
    public UserFindDto(Long id, String username, String nickname, String email) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
    }
}
