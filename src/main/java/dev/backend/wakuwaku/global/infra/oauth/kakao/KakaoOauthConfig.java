package dev.backend.wakuwaku.global.infra.oauth.kakao;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * application.yml에 oauth.kakao로 설정된 정보들을 통해 생성
 */
@Configuration
@Getter
public class KakaoOauthConfig{
        @Value("${oauth.kakao.redirect_uri}")
        String redirectUri;

        @Value("${oauth.kakao.client_id}")
        String clientId;

        @Value("${oauth.kakao.client_secret}")
        String clientSecret;

        @Value("${oauth.kakao.scope}")
        String[] scope;
}