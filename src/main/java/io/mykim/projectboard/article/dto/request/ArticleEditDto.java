package io.mykim.projectboard.article.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ArticleEditDto {
    @NotNull
    private Long id;
    @NotBlank(message = "글 제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "글 내용을 입력해주세요.")
    private String content;
    private String hashtags;

    public ArticleEditDto(Long id, String title, String content, String hashtags) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hashtags = hashtags;
    }

    public ArticleEditDto() {
    }
}
