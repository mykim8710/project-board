package io.mykim.projectboard.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArticleCreateDto {
    @NotBlank(message = "글 제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "글 내용을 입력해주세요.")
    private String content;
    private String hashtag;

    public ArticleCreateDto(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }
}
