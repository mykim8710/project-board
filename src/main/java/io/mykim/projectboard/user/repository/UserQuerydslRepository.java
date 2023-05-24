package io.mykim.projectboard.user.repository;

import io.mykim.projectboard.user.dto.response.UserFindDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQuerydslRepository  {
    Page<UserFindDto> findAllUser(Pageable pageable, String searchKeyword);
}
