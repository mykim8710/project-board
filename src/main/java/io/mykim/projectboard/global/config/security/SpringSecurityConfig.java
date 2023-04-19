package io.mykim.projectboard.global.config.security;

import io.mykim.projectboard.global.config.security.handler.CustomAccessDeniedHandler;
import io.mykim.projectboard.global.config.security.handler.CustomAuthenticationFailureHandler;
import io.mykim.projectboard.global.config.security.handler.CustomAuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Slf4j
@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 활성화 => 기본 스프링 필터체인에 등록
@EnableGlobalMethodSecurity(securedEnabled = true) // secure annotation 활성화
public class SpringSecurityConfig {
    // user password encoder 빈등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 인증을 무시할 경로들을 설정 >> static resource 보안설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().antMatchers("/css/**", "/js/**");
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


                .antMatchers("/users/**").anonymous()
                .antMatchers("/", "/articles", "/articles/{articleId}").permitAll()

                //


                .anyRequest().authenticated();

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
                .accessDeniedHandler(accessDeniedHandler()); // 접근권한이 없을때 handler

        // 로그아웃 설정
        httpSecurity
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)	                 // 로그아웃 후 세션 전체 삭제 여부
                .deleteCookies("JSESSIONID"); // 로그아웃 후 cookie 삭제


        return httpSecurity.build();
    }


    // CustomAuthenticationProvider가 자동으로 등록되어있음
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
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
