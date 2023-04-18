package io.mykim.projectboard.article.dto.response;

import io.mykim.projectboard.article.dto.request.ArticleSearchCondition;
import io.mykim.projectboard.global.select.pagination.CustomPaginationRequest;
import io.mykim.projectboard.global.select.pagination.CustomPaginationResponse;
import io.mykim.projectboard.global.select.sort.CustomSortingRequest;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ResponseArticleListDto {
    private List<ResponseArticleFindDto> responseArticleFindDtos;
    private CustomPaginationResponse paginationResponse;
    private CustomPaginationRequest paginationRequest;
    private CustomSortingRequest sortingRequest;

    private String sortingCondition;
    private String sortingDirection;

    private ArticleSearchCondition searchCondition;

    @Builder
    public ResponseArticleListDto(List<ResponseArticleFindDto> responseArticleFindDtos,
                                  CustomPaginationResponse paginationResponse,
                                  CustomPaginationRequest paginationRequest,
                                  CustomSortingRequest sortingRequest,
                                  ArticleSearchCondition searchCondition) {

        this.responseArticleFindDtos = responseArticleFindDtos;
        this.paginationResponse = paginationResponse;
        this.paginationRequest = paginationRequest;
        this.sortingRequest = sortingRequest;
        this.searchCondition = searchCondition;
        this.sortingCondition = sortingRequest.getSort().split("_")[0];
        this.sortingDirection = sortingRequest.getSort().split("_")[1];
    }
}
