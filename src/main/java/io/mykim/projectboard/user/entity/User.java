package io.mykim.projectboard.user.entity;

import io.mykim.projectboard.global.jpa.BaseTimeEntity;
import io.mykim.projectboard.user.dto.request.UserCreateDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(name = "user_name", length = 255, unique = true, nullable = false)
    private String username;
    @Column(name = "user_password", length = 255, nullable = false)
    private String password;
    @Column(name = "user_nickname", length = 100, unique = true, nullable = false)
    private String nickname;
    @Column(name = "user_email", length = 100, nullable = false)
    private String email;
    @Column(name = "user_memo", length = 255)
    private String memo;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", length = 50)
    private UserRole userRole;

    private User(UserCreateDto createDto) {
        this.username = createDto.getUsername();
        this.password = createDto.getPassword();
        this.nickname = createDto.getNickname();
        this.email = createDto.getEmail();
        this.memo = createDto.getMemo();
        this.userRole = UserRole.ROLE_USER;
    }

    private User(Long id, String username, String password, String nickname, String email, String memo, UserRole userRole) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.memo = memo;
        this.userRole = userRole;
    }

    public static User of(UserCreateDto createDto) {
        return new User(createDto);
    }

    public static User of(Long id, String username, String password, String nickname, String email, String memo, UserRole userRole) {
        return new User(id, username, password, nickname, email, memo, userRole);
    }
}
