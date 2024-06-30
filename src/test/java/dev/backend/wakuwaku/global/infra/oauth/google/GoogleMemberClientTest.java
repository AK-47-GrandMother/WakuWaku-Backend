package dev.backend.wakuwaku.global.infra.oauth.google;

import dev.backend.wakuwaku.domain.oauth.dto.OauthMember;
import dev.backend.wakuwaku.global.infra.oauth.google.client.GoogleApiClient;
import dev.backend.wakuwaku.global.infra.oauth.google.dto.GoogleMemberResponse;
import dev.backend.wakuwaku.global.infra.oauth.google.dto.GoogleToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GoogleMemberClientTest {

    @Mock
    private GoogleApiClient mockApiClient;

    @Mock
    private GoogleOauthConfig mockOauthConfig;

    @InjectMocks
    private GoogleMemberClient memberClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("구글 로그인 사용자 정보 가져오기")
    void testFetch() {
        // 테스트 데이터
        String authCode = "test12341234";
        String accessToken = "testAccessToken";
        String refreshToken = "testRefreshToken";
        Integer expiresIn = 3600;
        String tokenType = "Bearer";
        String idToken = "testIdToken";

        GoogleToken mockToken = new GoogleToken(accessToken, refreshToken, expiresIn, tokenType, idToken);
        GoogleMemberResponse mockResponse = new GoogleMemberResponse("dummyId", "dummy@example.com", "John", "Doe", "https://example.com/avatar.jpg");


        when(mockApiClient.fetchToken(any(MultiValueMap.class))).thenReturn(mockToken);
        when(mockApiClient.fetchMember(accessToken)).thenReturn(mockResponse);
        when(mockOauthConfig.getClientId()).thenReturn("mockClientId");
        when(mockOauthConfig.getClientSecret()).thenReturn("mockClientSecret");
        when(mockOauthConfig.getRedirectUri()).thenReturn("mockRedirectUri");
        when(mockOauthConfig.getTokenUri()).thenReturn("mockTokenUri");
        when(mockOauthConfig.getResourceUri()).thenReturn("mockResourceUri");

        // FRONT에서 넘겨주는 코드 받아서 사용자 정보 저장
        OauthMember result = memberClient.fetch(authCode);

        // THEN
        assertNotNull(result);
        assertEquals("dummyId", result.getOauthId().getOauthServerId());
        assertEquals("dummy@example.com", result.getEmail());
        assertEquals("John Doe", result.getNickname());
        assertEquals("https://example.com/avatar.jpg", result.getProfileImageUrl());

        // Verify interactions
        MultiValueMap<String, String> expectedTokenRequest = new LinkedMultiValueMap<>();
        expectedTokenRequest.add("grant_type", "authorization_code");
        expectedTokenRequest.add("client_id", "mockClientId");
        expectedTokenRequest.add("client_secret", "mockClientSecret");
        expectedTokenRequest.add("code", authCode);
        expectedTokenRequest.add("redirect_uri", "mockRedirectUri");
        expectedTokenRequest.add("token_uri", "mockTokenUri");
        expectedTokenRequest.add("resource_uri", "mockResourceUri");

        verify(mockApiClient).fetchToken(expectedTokenRequest);
        verify(mockApiClient).fetchMember(accessToken);

    }
}
