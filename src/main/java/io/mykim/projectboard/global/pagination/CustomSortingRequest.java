package io.mykim.projectboard.global.pagination;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
public class CustomSortingRequest {
    private static final String DEFAULT_PROPERTY = "id";
    private static final String SEPARATOR_SORT_CONDITION = "/";
    private static final String SEPARATOR_DIRECTION_PROPERTY = ",";

    private String sort;    // Custom rule : id,DESC/title,ASC......

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<Sort.Order> of() {
        if(!StringUtils.hasLength(this.sort)) {
            return List.of(new Sort.Order(Sort.Direction.DESC, DEFAULT_PROPERTY));
        }

        String[] sortArr = this.sort.split(SEPARATOR_SORT_CONDITION);

        return Arrays.stream(sortArr)
                .map(sort -> {
                    List<String> sortCondition = Arrays.asList(sort.split(SEPARATOR_DIRECTION_PROPERTY));
                    // 0 : property
                    // 1 : direction
                    String property = sortCondition.get(0);
                    String direction = sortCondition.get(1).toUpperCase();

                    return new Sort.Order(direction.equals("DESC") ? Sort.Direction.DESC : Sort.Direction.ASC, property);
                }).collect(Collectors.toList());
    }

    public CustomSortingRequest(String sort) {
        this.sort = sort;
    }
}
