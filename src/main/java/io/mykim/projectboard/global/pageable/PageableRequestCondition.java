package io.mykim.projectboard.global.pageable;

import io.mykim.projectboard.article.enums.SearchType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Sort;

@Getter
@ToString
public class PageableRequestCondition {
    private long page;
    private int size;
    private String sortCondition;
    private String sortDirection;
    private String searchType;
    private String searchKeyword;

    @Builder
    public PageableRequestCondition(long page, int size, Sort sort, SearchType searchType, String searchKeyword) {
        String[] split = sort.toString().split(": ");

        this.page = page;
        this.size = size;
        this.sortCondition = split[0];
        this.sortDirection = split[1];
        this.searchType = searchType == null ? SearchType.ALL.name() : searchType.name();
        this.searchKeyword = searchKeyword;
    }
}
