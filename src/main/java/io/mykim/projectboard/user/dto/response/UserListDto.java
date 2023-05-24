package io.mykim.projectboard.user.dto.response;

import io.mykim.projectboard.global.pageable.CustomPaginationResponse;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class UserListDto {
    private List<UserFindDto> userFindDtos;
    private CustomPaginationResponse paginationResponse;

    @Builder
    public UserListDto(List<UserFindDto> userFindDtos, CustomPaginationResponse paginationResponse) {
        this.userFindDtos = userFindDtos;
        this.paginationResponse = paginationResponse;
    }
}
