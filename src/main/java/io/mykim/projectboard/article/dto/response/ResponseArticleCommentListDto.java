package io.mykim.projectboard.article.dto.response;

import io.mykim.projectboard.global.select.pagination.CustomPaginationResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
public class ResponseArticleCommentListDto {
    private List<ResponseArticleCommentFindDto> responseArticleCommentFindDtos;
    private CustomPaginationResponse paginationResponse;

    @Builder
    public ResponseArticleCommentListDto(List<ResponseArticleCommentFindDto> responseArticleCommentFindDtos, CustomPaginationResponse paginationResponse) {
        this.responseArticleCommentFindDtos = responseArticleCommentFindDtos;
        this.paginationResponse = paginationResponse;
    }
}
