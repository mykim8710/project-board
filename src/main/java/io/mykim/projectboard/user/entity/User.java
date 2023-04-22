package io.mykim.projectboard.user.entity;

import io.mykim.projectboard.global.config.jpa.BaseTimeEntity;
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
    @Column(name = "user_email", length = 100, unique = true, nullable = false)
    private String email;
    @Column(name = "user_memo", length = 255)
    private String memo;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", length = 50)
    private UserType userType;

    // [양방향 매핑]
    // user - articles, 1 : N
    // 연관관계의 주인 : articles이 userId(fk)를 가짐
//    @ToString.Exclude
//    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL) // 유저가 지워지면 유저와 연관된 게시글도 삭제
//    private List<Article> articles = new ArrayList<>();

    // [양방향 매핑]
    // user - articleComment, 1 : N
    // 연관관계의 주인 : articleComment가 userId(fk)를 가짐
//    @ToString.Exclude
//    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL) // 유저가 지워지면 유저와 연관된 댓글도 삭제
//    private List<ArticleComment> articleComments = new ArrayList<>();

    private User(UserCreateDto createDto) {
        this.username = createDto.getUsername();
        this.password = createDto.getPassword();
        this.nickname = createDto.getNickname();
        this.email = createDto.getEmail();
        this.memo = createDto.getMemo();
        this.userType = UserType.GENERAL;
    }

    private User(Long id, String username, String password, String nickname, String email, String memo, UserType userType) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.memo = memo;
        this.userType = userType;
    }

    public static User of(UserCreateDto createDto) {
        return new User(createDto);
    }

    public static User of(Long id, String username, String password, String nickname, String email, String memo, UserType userType) {
        return new User(id, username, password, nickname, email, memo, userType);
    }
}
