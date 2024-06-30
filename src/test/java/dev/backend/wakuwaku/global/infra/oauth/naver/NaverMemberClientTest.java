package dev.backend.wakuwaku.global.infra.oauth.naver;

import dev.backend.wakuwaku.domain.oauth.dto.OauthMember;
import dev.backend.wakuwaku.domain.oauth.dto.OauthServerType;
import dev.backend.wakuwaku.global.infra.oauth.naver.client.NaverApiClient;
import dev.backend.wakuwaku.global.infra.oauth.naver.dto.NaverMemberResponse;
import dev.backend.wakuwaku.global.infra.oauth.naver.dto.NaverToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class NaverMemberClientTest {

    @Mock
    private NaverApiClient naverApiClient;

    @Mock
    private NaverOauthConfig naverOauthConfig;

    private NaverMemberClient naverMemberClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        naverMemberClient = new NaverMemberClient(naverApiClient, naverOauthConfig);
    }

    @Test
    @DisplayName("네이버 로그인 사용자 정보 가져오기")
    void testFetch() {
        // 네이버 토큰 테스트 데이터
        NaverToken tokenInfo = new NaverToken("accessToken", "refreshToken", "tokenType", 3600, null, null);
        when(naverApiClient.fetchToken(any())).thenReturn(tokenInfo);

        // 사용자 정보 테스트데이터
        NaverMemberResponse.Response response = new NaverMemberResponse.Response(
                "naver123", "testUser", "Test User", "test@example.com",
                "male", "30", "1990-01-01", "http://example.com/profile.jpg",
                "1990", "01012345678"
        );
        NaverMemberResponse naverMemberResponse = new NaverMemberResponse("200", "success", response);
        when(naverApiClient.fetchMember(any())).thenReturn(naverMemberResponse);

        // FRONT에서 넘겨주는 코드를 받아서 사용자 정보를 받아 저장함.
        OauthMember oauthMember = naverMemberClient.fetch("authCode");

        // 사용자 정보 맞는지 확인
        assertEquals(OauthServerType.NAVER, oauthMember.getOauthId().getOauthServerType());
        assertEquals("naver123", oauthMember.getOauthId().getOauthServerId());
        assertEquals("testUser", oauthMember.getNickname());
        assertEquals("http://example.com/profile.jpg", oauthMember.getProfileImageUrl());
        assertEquals("test@example.com", oauthMember.getEmail());
        assertEquals("1990-01-01", oauthMember.getBirthday());
    }
}
