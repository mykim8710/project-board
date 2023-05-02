package io.mykim.projectboard.article.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class ResponseHashtagListDto {
    private List<String> hashtags;
    private int page;            // 현재 페이지 번호
    private boolean hasNextPage; // 다음 페이지 존재 여부
    private boolean isLast;      // 현재 페이지가 마지막 페이지인지 여부

    @Builder
    public ResponseHashtagListDto(List<String> hashtags, int page, boolean hasNextPage, boolean isLast) {
        this.hashtags = hashtags;
        this.page = page;
        this.hasNextPage = hasNextPage;
        this.isLast = isLast;
    }
}
