package io.mykim.projectboard.user.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import io.mykim.projectboard.user.dto.response.QUserFindDto;
import io.mykim.projectboard.user.dto.response.UserFindDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static io.mykim.projectboard.global.jpa.QuerydslUtils.getOrderSpecifier;
import static io.mykim.projectboard.user.entity.QUser.user;

public class UserQuerydslRepositoryImpl implements UserQuerydslRepository {
    private final JPAQueryFactory queryFactory;

    public UserQuerydslRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<UserFindDto> findAllUser(Pageable pageable, String searchKeyword) {
        List<UserFindDto> userFindDtos = queryFactory
                                            .select(new QUserFindDto(user.id, user.username, user.nickname, user.email, user.createdAt, user.lastModifiedAt))
                                            .from(user)
                                            .where(createUniversalSearchCondition(searchKeyword))
                                            .orderBy(getOrderSpecifier(pageable.getSort(), user.getType(), user.getMetadata())
                                                        .stream()
                                                        .toArray(OrderSpecifier[]::new))
                                            .offset(pageable.getOffset())
                                            .limit(pageable.getPageSize())
                                            .fetch();

        Long count = queryFactory
                        .select(user.count())
                        .from(user)
                        .where(createUniversalSearchCondition(searchKeyword))
                        .fetchOne();

        return new PageImpl<>(userFindDtos, pageable, count);
    }

    private BooleanExpression createUniversalSearchCondition(String keyword) {
        return !StringUtils.hasLength(keyword) ? null : user.username.contains(keyword)
                                                            .or(user.nickname.contains(keyword))
                                                            .or(user.email.contains(keyword));
    }
}
