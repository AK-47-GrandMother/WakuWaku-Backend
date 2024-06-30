package dev.backend.wakuwaku.global.infra.oauth.naver.authcode;


import dev.backend.wakuwaku.domain.oauth.dto.OauthServerType;
import dev.backend.wakuwaku.global.infra.oauth.oauthcode.OauthCodeRequestUrlProvider;
import dev.backend.wakuwaku.global.infra.oauth.naver.NaverOauthConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class NaverOauthCodeRequestUrlProvider implements OauthCodeRequestUrlProvider {

    private final NaverOauthConfig naverOauthConfig;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.NAVER;
    }

    @Override
    public String provide() {
        return UriComponentsBuilder
                .fromUriString("https://nid.naver.com/oauth2.0/authorize")
                .queryParam("response_type", "code")
                .queryParam("client_id", naverOauthConfig.getClientId())
                .queryParam("redirect_uri", naverOauthConfig.getRedirectUri())
                //.queryParam("state", "samplestate") // 이건 나중에 따로 찾아보고 설정해서 쓰면 됨
                // XSS 공격 방지용 인코딩
                .build()
                .toUriString();
    }
}