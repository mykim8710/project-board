package io.mykim.projectboard.user.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserFindDto {
    private Long id;
    private String username;
    private String nickname;
    private String email;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;

    @QueryProjection
    public UserFindDto(Long id, String username, String nickname, String email, LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }
}
