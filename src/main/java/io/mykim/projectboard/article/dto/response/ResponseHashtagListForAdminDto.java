package io.mykim.projectboard.article.dto.response;

import io.mykim.projectboard.global.pageable.CustomPaginationResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ResponseHashtagListForAdminDto {
    private List<ResponseHashtagFindDto> responseHashtagFindDtos;
    private CustomPaginationResponse paginationResponse;

    @Builder
    public ResponseHashtagListForAdminDto(List<ResponseHashtagFindDto> responseHashtagFindDtos, CustomPaginationResponse paginationResponse) {
        this.responseHashtagFindDtos = responseHashtagFindDtos;
        this.paginationResponse = paginationResponse;
    }
}
