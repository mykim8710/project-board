package io.mykim.projectboard.dto.response;

import io.mykim.projectboard.global.pagination.CustomPaginationResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ResponseArticleListDto {
    private List<ResponseArticleFindDto> responseArticleFindDtos;
    private CustomPaginationResponse paginationResponse;

    @Builder
    public ResponseArticleListDto(List<ResponseArticleFindDto> responseArticleFindDtos, CustomPaginationResponse paginationResponse) {
        this.responseArticleFindDtos = responseArticleFindDtos;
        this.paginationResponse = paginationResponse;
    }
}
