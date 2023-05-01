package io.mykim.projectboard.article.dto.request;

import io.mykim.projectboard.article.enums.SearchType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class ArticleSearchCondition {
    private String keyword;
    private SearchType searchType;

    public ArticleSearchCondition(String keyword, SearchType searchType) {
        this.keyword = keyword;
        this.searchType = searchType;
    }
}
