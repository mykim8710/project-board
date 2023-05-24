package io.mykim.projectboard.article.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.mykim.projectboard.article.entity.Hashtag;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class ResponseHashtagFindDto {
    private Long id;
    private String name;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy/MM/dd HH:mm:ss")
    private LocalDateTime lastModifiedAt;

    private ResponseHashtagFindDto(Long id, String name, LocalDateTime createdAt, LocalDateTime lastModifiedAt) {
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public static ResponseHashtagFindDto from(Hashtag hashtag) {
        return new ResponseHashtagFindDto(hashtag.getId(), hashtag.getName(), hashtag.getCreatedAt(), hashtag.getLastModifiedAt());
    }
}
