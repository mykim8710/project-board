package io.mykim.projectboard.global.config.security.dto;

import io.mykim.projectboard.global.config.security.enums.OAuth2ProviderEnums;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String providerId;
    private String nameAttributeKey;
    private String nickname;
    private String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String providerId, String nameAttributeKey, String nickname, String email) {
        this.attributes = attributes;
        this.providerId = providerId;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.email = email;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if(registrationId.equals(OAuth2ProviderEnums.NAVER.getRegistrationId())) {
            return ofNaver(userNameAttributeName, attributes);
        }

        if(registrationId.equals(OAuth2ProviderEnums.KAKAO.getRegistrationId())) {
            return ofKakao(userNameAttributeName, attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                                .providerId((String)attributes.get("sub"))
                                .nickname((String)attributes.get("name"))
                                .email((String)attributes.get("email"))
                                .nameAttributeKey(userNameAttributeName) // sub
                                .attributes(attributes)
                                .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>)attributes.get(userNameAttributeName);

        return OAuthAttributes.builder()
                                .providerId((String)response.get("id"))
                                .nickname((String)response.get("nickname"))
                                .email((String)response.get("email"))
                                .nameAttributeKey(userNameAttributeName) // response
                                .attributes(attributes)
                                .build();
    }

    public static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>)attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>)kakaoAccount.get("profile");

        return OAuthAttributes.builder()
                                .providerId(String.valueOf((Long)attributes.get("id")))
                                .nickname((String)profile.get("nickname"))
                                .email((String)kakaoAccount.get("email"))
                                .nameAttributeKey(userNameAttributeName) // id
                                .attributes(attributes)
                                .build();
    }

}
