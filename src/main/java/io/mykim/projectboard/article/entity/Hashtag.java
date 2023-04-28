package io.mykim.projectboard.article.entity;

import io.mykim.projectboard.global.config.jpa.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true) // BaseTimeEntity 항목까지 출력
@Getter
@Entity
@Table(name = "hashtag",
        indexes = {
            @Index(columnList = "hashtag_name")
})
public class Hashtag extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id", nullable = false)
    private Long id;

    @Column(name = "hashtag_name", nullable = false, unique = true, length = 50)
    private String name;

    private Hashtag(String name) {
        this.name = name;
    }

    public static Hashtag of(String name) {
        return new Hashtag(name);
    }
}
