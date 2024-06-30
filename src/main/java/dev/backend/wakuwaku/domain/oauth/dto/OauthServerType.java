package dev.backend.wakuwaku.domain.oauth.dto;

import static java.util.Locale.ENGLISH;


/**
 * 이는 카카오, 구글, 네이버 등 Oauth2.0 인증을 제공하는 서버의 종류를 명시할 Enum
 * "kakao"를 통해 OauthServerType.KAKAO를 찾아오기 위해 fromName() 이라는 메서드를 구현
 */
public enum OauthServerType {

    KAKAO,
    NAVER,

    GOOGLE,
    ;

    public static OauthServerType fromName(String type) {
        return OauthServerType.valueOf(type.toUpperCase(ENGLISH));
    }
}