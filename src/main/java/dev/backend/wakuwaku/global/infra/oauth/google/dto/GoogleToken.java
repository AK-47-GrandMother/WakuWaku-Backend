package dev.backend.wakuwaku.global.infra.oauth.google.dto;

import com.fasterxml.jackson.databind.annotation.JsonNaming;
import static com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;

@JsonNaming(value = SnakeCaseStrategy.class)
public record GoogleToken(
        String access_token,
        String token_type,
        Integer expires_in,
        String refresh_token,
        String id_token
) {
}
