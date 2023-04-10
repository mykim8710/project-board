package io.mykim.projectboard.global.pagination;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CustomPaginationRequest {
    private static final int DEFAULT_OFFSET_VALUE = 1;
    private static final int DEFAULT_LIMIT_VALUE = 10;
    private static final int MAX_LIMIT_VALUE = 2000;

    private int offset; // 페이지 번호, ~ 번째 페이지부터 시작할 것인가?
    private int limit;  // 가져올 개수, 한번 조회 시 ~ 개를 가져올 것인가?

    public void setOffset(int offset) {
        this.offset = Math.max(DEFAULT_OFFSET_VALUE, offset);
    }

    public void setLimit(int limit) {
        this.limit = limit > MAX_LIMIT_VALUE ? DEFAULT_LIMIT_VALUE : limit;
    }

    public CustomPaginationRequest(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }
}
