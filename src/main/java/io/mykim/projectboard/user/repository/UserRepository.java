package io.mykim.projectboard.user.repository;

import io.mykim.projectboard.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserQuerydslRepository {
    Boolean existsByUsername(String username);
    // Boolean existsByEmail(String email);
    Boolean existsByNickname(String nickname);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

}
