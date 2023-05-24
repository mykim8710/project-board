package io.mykim.projectboard.article.dto.response;

import io.mykim.projectboard.global.pageable.CustomPaginationResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class ResponseArticleCommentListDto {
    private List<ResponseArticleCommentFindDto> articleCommentFindDtos;
    private CustomPaginationResponse paginationResponse;

    @Builder
    public ResponseArticleCommentListDto(List<ResponseArticleCommentFindDto> articleCommentFindDtos, CustomPaginationResponse paginationResponse) {
        this.articleCommentFindDtos = articleCommentFindDtos;
        this.paginationResponse = paginationResponse;
    }
}
