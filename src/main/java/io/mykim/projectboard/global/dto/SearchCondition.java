package io.mykim.projectboard.global.dto;

import io.mykim.projectboard.article.enums.SearchType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class SearchCondition {
    private String keyword;
    private SearchType searchType;

    public SearchCondition(String keyword, SearchType searchType) {
        this.keyword = keyword;
        this.searchType = searchType;
    }
}
