package dev.backend.wakuwaku.global.infra.oauth.kakao.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * @JsonNaming(SnakeCaseStrategy.class)를 통해 token_type과 같은 응답을 tokenType으로 받아올 수 있음.
 * */
@JsonNaming(SnakeCaseStrategy.class)
public record KakaoToken(
        String tokenType,
        String accessToken,
        String idToken,
        Integer expiresIn,
        String refreshToken,
        Integer refreshTokenExpiresIn,
        String scope
) {
}