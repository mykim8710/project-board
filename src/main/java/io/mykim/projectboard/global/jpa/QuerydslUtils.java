package io.mykim.projectboard.global.jpa;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class QuerydslUtils {
    public static List<OrderSpecifier> getOrderSpecifier(Sort sort, Class type, PathMetadata metadata) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach (order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String orderProperty = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(type, metadata);
            orders.add(new OrderSpecifier(direction, orderByExpression.get(orderProperty)));
        });

        return orders;
    }
}
