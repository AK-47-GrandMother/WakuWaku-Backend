package dev.backend.wakuwaku.global.infra.oauth.google;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class GoogleOauthConfig{

        @Value("${oauth.google.redirect-uri}")
        String redirectUri;

        @Value("${oauth.google.client-id}")
        String clientId;

        @Value("${oauth.google.client-secret}")
        String clientSecret;

        @Value("${oauth.google.scope}")
        String[] scope;

        @Value("${oauth.google.token-uri}")
        String tokenUri;

        @Value("${oauth.google.resource-uri}")
        String resourceUri;

        //state
}
