package dev.backend.wakuwaku.global.infra.oauth.kakao;

import dev.backend.wakuwaku.domain.oauth.dto.OauthMember;
import dev.backend.wakuwaku.domain.oauth.dto.OauthServerType;
import dev.backend.wakuwaku.global.infra.oauth.kakao.client.KakaoApiClient;
import dev.backend.wakuwaku.global.infra.oauth.kakao.dto.KakaoMemberResponse;
import dev.backend.wakuwaku.global.infra.oauth.kakao.dto.KakaoToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class KakaoMemberClientTest {

    @Mock
    private KakaoApiClient kakaoApiClient;

    @Mock
    private KakaoOauthConfig kakaoOauthConfig;

    private KakaoMemberClient kakaoMemberClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        kakaoMemberClient = new KakaoMemberClient(kakaoApiClient, kakaoOauthConfig);
    }

    @Test
    @DisplayName("카카오 로그인 사용자 정보 가져오기")
    void testFetch() {
        // 카카오 토큰 테스트 데이터
        KakaoToken tokenInfo = new KakaoToken("bearer", "accessToken", "idToken", 3600, "refreshToken", 7200, "profile");
        when(kakaoApiClient.fetchToken(any())).thenReturn(tokenInfo);

        // 사용자 정보 테스트데이터
        KakaoMemberResponse.Profile profile = new KakaoMemberResponse.Profile(
                "testUser", "http://example.com/thumbnail.jpg", "http://example.com/profile.jpg", true
        );

        // 사용자의 프로필 정보에 대한 동의 여부
        KakaoMemberResponse.KakaoAccount kakaoAccount = new KakaoMemberResponse.KakaoAccount(
                true, true, true, profile, true, "Test User", true, true, true,
                "test@example.com", true, "30-39", true, "1990", true, "01-01",
                "SOLAR", true, "male", true, "01012345678", true, "CI123", null
        );
        KakaoMemberResponse kakaoMemberResponse = new KakaoMemberResponse(123L, true, null, kakaoAccount);

        when(kakaoApiClient.fetchMember(any())).thenReturn(kakaoMemberResponse);

        // FRONT에서 넘겨주는 코드를 받아서 사용자 정보를 받아 저장함.
        OauthMember oauthMember = kakaoMemberClient.fetch("authCode");

        // 사용자 정보 맞는지 확인
        assertEquals(OauthServerType.KAKAO, oauthMember.getOauthId().getOauthServerType());
        assertEquals("123", oauthMember.getOauthId().getOauthServerId());
        assertEquals("testUser", oauthMember.getNickname());
        assertEquals("http://example.com/profile.jpg", oauthMember.getProfileImageUrl());
        assertEquals("test@example.com", oauthMember.getEmail());
    }
}
