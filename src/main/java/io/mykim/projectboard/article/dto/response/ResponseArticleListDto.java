package io.mykim.projectboard.article.dto.response;

import io.mykim.projectboard.global.pageable.CustomPaginationResponse;
import io.mykim.projectboard.global.pageable.PageableRequestCondition;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ResponseArticleListDto {
    private List<ResponseArticleFindDto> responseArticleFindDtos;
    private CustomPaginationResponse paginationResponse;

    private PageableRequestCondition pageableRequestCondition;

    @Builder
    public ResponseArticleListDto(List<ResponseArticleFindDto> responseArticleFindDtos,
                                  CustomPaginationResponse paginationResponse,
                                  PageableRequestCondition pageableRequestCondition) {

        this.responseArticleFindDtos = responseArticleFindDtos;
        this.paginationResponse = paginationResponse;
        this.pageableRequestCondition = pageableRequestCondition;
    }
}
