package io.mykim.projectboard.article.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
public class ArticleCreateDto {
    @NotBlank(message = "글 제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "글 내용을 입력해주세요.")
    private String content;
    private String hashtags;

    public ArticleCreateDto(String title, String content, String hashtags) {
        this.title = title;
        this.content = content;
        this.hashtags = hashtags;
    }

    public ArticleCreateDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public ArticleCreateDto() {
    }
}
