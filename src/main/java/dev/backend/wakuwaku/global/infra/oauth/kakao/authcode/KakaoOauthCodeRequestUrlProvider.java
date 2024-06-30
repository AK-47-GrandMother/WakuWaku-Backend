package dev.backend.wakuwaku.global.infra.oauth.kakao.authcode;


import dev.backend.wakuwaku.domain.oauth.dto.OauthServerType;
import dev.backend.wakuwaku.global.infra.oauth.kakao.KakaoOauthConfig;
import dev.backend.wakuwaku.global.infra.oauth.oauthcode.OauthCodeRequestUrlProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *  설정한 정보를 담고 있는 클래스
 */

@Component
@RequiredArgsConstructor
public class KakaoOauthCodeRequestUrlProvider implements OauthCodeRequestUrlProvider {

    private final KakaoOauthConfig kakaoOauthConfig;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.KAKAO;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com/oauth/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", kakaoOauthConfig.getClientId())
                .queryParam("redirect_uri", kakaoOauthConfig.getRedirectUri())
                .queryParam("scope", String.join(",", kakaoOauthConfig.getScope()))
                .toUriString();
    }
}