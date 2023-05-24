package io.mykim.projectboard.article.dto.request;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class HashtagCreateDto {
    private String name;

    public HashtagCreateDto() {
    }

    public HashtagCreateDto(String name) {
        this.name = name;
    }
}
