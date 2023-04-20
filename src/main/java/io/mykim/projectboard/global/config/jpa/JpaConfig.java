package io.mykim.projectboard.global.config.jpa;

import io.mykim.projectboard.global.config.security.PrincipalDetail;
import io.mykim.projectboard.user.entity.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig implements AuditorAware<User> {
    // @CreatedBy, @LastModifiedBy에 대해 아래에 설정한 값으로 넣에 줄 예정
    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        return Optional.of((User) authentication.getPrincipal());
    }
}
