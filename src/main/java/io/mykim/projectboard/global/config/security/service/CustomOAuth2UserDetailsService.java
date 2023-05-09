package io.mykim.projectboard.global.config.security.service;

import io.mykim.projectboard.global.config.security.dto.PrincipalDetail;
import io.mykim.projectboard.global.config.security.dto.OAuthAttributes;
import io.mykim.projectboard.user.entity.User;
import io.mykim.projectboard.user.entity.UserRole;
import io.mykim.projectboard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserDetailsService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService(); // delegate, 대리자
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        /**
         * registrationId : 현재 로그인을 진행 중인 서비스를 구분하는 코드
         * 구글 로그인은 불필요하지만 이후 네이버, 카카오 로그인 연동시에 어떤 서비스인지 판별하기 위해 사용
         */

        String userNameAttributeName = userRequest.getClientRegistration()
                                                    .getProviderDetails()
                                                    .getUserInfoEndpoint()
                                                    .getUserNameAttributeName();
        /**
         * userNameAttributeName : OAuth2 로그인 진행 시 키가되는 필드값을 이야기함(primary key와 같음)
         * 구글의 경우 기본적으로 코드(= sub)를 지원, 네이버, 카카오등은 기본 지원하지 않음
         * 구글, 네이버, 카카오 동시 지원할 때 사용
         */

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        /**
         * OAuthAttributes : OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스
         */
        String providerId = oAuthAttributes.getProviderId();
        String username = registrationId +"_" +providerId;
        String dummyPassword = passwordEncoder.encode(UUID.randomUUID().toString());
        String email = oAuthAttributes.getEmail();

        User user = userRepository.findByEmail(email)
                .orElse(User.of(null,
                                        username,
                                        dummyPassword,
                        registrationId +"_" +oAuthAttributes.getNickname() + "_" + UUID.randomUUID(),
                                        oAuthAttributes.getEmail(),
                                        "",
                                        UserRole.ROLE_USER));

        userRepository.save(user);
        return new PrincipalDetail(user, oAuthAttributes.getAttributes());
    }
}
