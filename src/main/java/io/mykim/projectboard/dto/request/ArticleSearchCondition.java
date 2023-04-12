package io.mykim.projectboard.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ArticleSearchCondition {
    private String keyword;
    private String searchType; // title, content, hashtag, createBy

    public ArticleSearchCondition(String keyword, String searchType) {
        this.keyword = keyword;
        this.searchType = searchType;
    }
}
