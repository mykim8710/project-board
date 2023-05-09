package io.mykim.projectboard.global.config.security;

import io.mykim.projectboard.global.config.security.dto.PrincipalDetail;
import io.mykim.projectboard.global.config.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

// 화면에서 입력한 로그인 정보와 DB에서 가져온 사용자의 정보를 비교해주는 interface >> authenticate 메서드
// not used
@Slf4j
@RequiredArgsConstructor
//@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        PrincipalDetail principalDetail = (PrincipalDetail)customUserDetailsService.loadUserByUsername(username);

        // 비밀번호가 미일치 throw Exception
        if(!passwordEncoder.matches(password, principalDetail.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return new UsernamePasswordAuthenticationToken(principalDetail.getUser(), null, principalDetail.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;    // false -> true
    }
}