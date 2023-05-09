package io.mykim.projectboard.global.config.security.enums;

import lombok.Getter;

@Getter
public enum OAuth2ProviderEnums {
    KAKAO("kakao"),
    NAVER("naver"),
    GOOGLE("google"),
    ;

    private String registrationId;

    OAuth2ProviderEnums(String registrationId) {
        this.registrationId = registrationId;
    }
}
