package io.mykim.projectboard.global.config.security;

import io.mykim.projectboard.global.config.security.handler.*;
import io.mykim.projectboard.global.config.security.service.CustomOAuth2UserDetailsService;
import io.mykim.projectboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationFailureHandler;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 활성화 => 기본 스프링 필터체인에 등록
@EnableGlobalMethodSecurity(securedEnabled = true) // secure annotation 활성화
public class SpringSecurityConfig {
    private final UserRepository userRepository;

    @Bean
    public CustomOAuth2UserDetailsService customOAuth2UserDetailsService() {
        return new CustomOAuth2UserDetailsService(userRepository, passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Session 방식
     */
    @Bean
    public SecurityFilterChain filterChainSession(HttpSecurity httpSecurity) throws Exception {
        // csrf token disable
        httpSecurity
                .csrf().disable();

        // 권한 별 url 접근설정
        httpSecurity
                .authorizeRequests()
                    .antMatchers("/articles/create", "/articles/{articleId}/edit", "/articles/{articleId}/delete").authenticated()
                    .antMatchers(HttpMethod.GET,"/api/v1/articles/{articleId}/article-comments/{articleCommentId}").authenticated()
                    .antMatchers(HttpMethod.POST, "/api/v1/articles/{articleId}/article-comments").authenticated()
                    .antMatchers(HttpMethod.PATCH,"/api/v1/articles/{articleId}/article-comments/{articleCommentId}").authenticated()
                    .antMatchers(HttpMethod.DELETE,"/api/v1/articles/{articleId}/article-comments/{articleCommentId}").authenticated()

                    .antMatchers("/users/**", "/api/v1/users/**").anonymous()
                    .antMatchers(HttpMethod.GET, "/api/v1/articles/{articleId}/article-comments").permitAll()
                    .antMatchers(HttpMethod.GET, "/", "/profile", "/error-page/*", "/articles", "/articles/{articleId}", "/api/v1/articles/{articleId}/article-comments").permitAll()
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll();

        // 로그인 설정
        httpSecurity
                .formLogin()
                .loginPage("/users/sign-in")
                .loginProcessingUrl("/users/sign-in")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(authenticationSuccessHandler())
                .failureHandler(authenticationFailureHandler());

        // Exception 설정
        httpSecurity
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler()); // 접근권한이 없을때 handler

        // 로그아웃 설정
        httpSecurity
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)	 // 로그아웃 후 세션 전체 삭제 여부
                .deleteCookies("JSESSIONID"); // 로그아웃 후 cookie 삭제

        // oauth 설정
        httpSecurity
                .oauth2Login() // oauth2 로그인기능 설정의 진입점
                    .userInfoEndpoint() // oauth2 로그인 성공이후 사용자 정보를 가져올때의 설정들을 담당
                    .userService(customOAuth2UserDetailsService()) ; // 소셜 로그인 성공 시 후속조치를 진행할 UserService 인터페이스 구현체를 등록(소셜 서비스 에서 사용자정보를 가졍노 상태에서 추가로 진행하고자하는 기능들을 명시)

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }

}
