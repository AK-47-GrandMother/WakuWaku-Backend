package dev.backend.wakuwaku.global.infra.oauth.naver;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@Getter
public class NaverOauthConfig{

        @Value("${oauth.naver.redirect_uri}")
        String redirectUri;

        @Value("${oauth.naver.client_id}")
        String clientId;

        @Value("${oauth.naver.client_secret}")
        String clientSecret;
        @Value("${oauth.naver.scope}")
        String[] scope;
        // state
}