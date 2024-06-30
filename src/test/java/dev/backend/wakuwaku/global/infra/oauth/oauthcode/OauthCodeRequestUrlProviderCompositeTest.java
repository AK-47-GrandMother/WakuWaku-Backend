package dev.backend.wakuwaku.global.infra.oauth.oauthcode;

import dev.backend.wakuwaku.domain.oauth.dto.OauthServerType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class OauthCodeRequestUrlProviderCompositeTest {

    @Mock
    private OauthCodeRequestUrlProvider googleProvider;

    @Mock
    private OauthCodeRequestUrlProvider naverProvider;

    @Mock
    private OauthCodeRequestUrlProvider kakaoProvider;

    private OauthCodeRequestUrlProviderComposite composite;

    @BeforeEach
    void setUp() {
        Set<OauthCodeRequestUrlProvider> providers = Set.of(
                googleProvider,
                naverProvider,
                kakaoProvider
        );

        // 구글 PROVIDER
        lenient().when(googleProvider.supportServer()).thenReturn(OauthServerType.GOOGLE);
        lenient().when(googleProvider.provide()).thenReturn("https://google.com/oauth");

        // 네이버 PROVIDER
        lenient().when(naverProvider.supportServer()).thenReturn(OauthServerType.NAVER);
        lenient().when(naverProvider.provide()).thenReturn("https://nid.naver.com/oauth2.0/authorize");

        // 카카오 PROVIDER
        lenient().when(kakaoProvider.supportServer()).thenReturn(OauthServerType.KAKAO);
        lenient().when(kakaoProvider.provide()).thenReturn("https://kauth.kakao.com/oauth/authorize");

        composite = new OauthCodeRequestUrlProviderComposite(providers);
    }

    @Test
    @DisplayName("제공되는 URL 일치 테스트 - Google")
    void testProvideGoogle() {
        String providedUrl = composite.provide(OauthServerType.GOOGLE);
        assertEquals("https://google.com/oauth", providedUrl);
    }

    @Test
    @DisplayName("제공되는 URL 일치 테스트 - Naver")
    void testProvideNaver() {
        String providedUrl = composite.provide(OauthServerType.NAVER);
        assertEquals("https://nid.naver.com/oauth2.0/authorize", providedUrl);
    }

    @Test
    @DisplayName("제공되는 URL 일치 테스트 - Kakao")
    void testProvideKakao() {
        String providedUrl = composite.provide(OauthServerType.KAKAO);
        assertEquals("https://kauth.kakao.com/oauth/authorize", providedUrl);
    }
}
