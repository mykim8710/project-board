package io.mykim.projectboard.config.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@Configuration
@EnableJpaAuditing
public class JpaConfig {
    // @CreatedBy에 대해 아래에 설정한 값으로 넣에 줄 예정
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("mykim");  // TODO : 스프링시큐리티, 인증기능 구현시 수정 예정, "mykim"은 임의의 데이터
    }



}
