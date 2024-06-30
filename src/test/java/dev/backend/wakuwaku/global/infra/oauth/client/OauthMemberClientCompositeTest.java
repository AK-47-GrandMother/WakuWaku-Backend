package dev.backend.wakuwaku.global.infra.oauth.client;

import dev.backend.wakuwaku.domain.oauth.dto.OauthMember;
import dev.backend.wakuwaku.domain.oauth.dto.OauthServerType;
import dev.backend.wakuwaku.global.infra.oauth.google.GoogleOauthConfig;
import dev.backend.wakuwaku.global.infra.oauth.google.client.GoogleApiClient;
import dev.backend.wakuwaku.global.infra.oauth.google.GoogleMemberClient;
import dev.backend.wakuwaku.global.infra.oauth.google.dto.GoogleMemberResponse;
import dev.backend.wakuwaku.global.infra.oauth.google.dto.GoogleToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class OauthMemberClientCompositeTest {

    private OauthMemberClientComposite clientComposite;

    @Mock
    private GoogleApiClient mockGoogleApiClient;

    @Mock
    private GoogleOauthConfig mockGoogleOauthConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock 객체를 사용하여 OauthMemberClientComposite 생성
        GoogleMemberClient googleMemberClient = new GoogleMemberClient(mockGoogleApiClient, mockGoogleOauthConfig);
        clientComposite = new OauthMemberClientComposite(Collections.singleton(googleMemberClient));
    }

    @Test
    @DisplayName("Oauth Login Type에 관련된 사용자 정보 가져오기")
    void testFetch() {
        // Given
        String mockAuthCode = "mockAuthCode";
        OauthServerType oauthServerType = OauthServerType.GOOGLE;
        GoogleToken mockToken = new GoogleToken("mockAccessToken", "bearer", 3600, null, "mockIdToken");
        GoogleMemberResponse mockResponse = new GoogleMemberResponse("12345", "test@gmail.com", "John", "Doe", "https://example.com/picture");

        // Mock GoogleOauthConfig에서 필요한 값들 설정
        when(mockGoogleOauthConfig.getClientId()).thenReturn("mockClientId");
        when(mockGoogleOauthConfig.getClientSecret()).thenReturn("mockClientSecret");
        when(mockGoogleOauthConfig.getRedirectUri()).thenReturn("mockRedirectUri");
        when(mockGoogleOauthConfig.getTokenUri()).thenReturn("mockTokenUri");
        when(mockGoogleOauthConfig.getResourceUri()).thenReturn("mockResourceUri");

        // MultiValueMap 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", mockGoogleOauthConfig.getClientId());
        params.add("client_secret", mockGoogleOauthConfig.getClientSecret());
        params.add("code", mockAuthCode);
        params.add("redirect_uri", mockGoogleOauthConfig.getRedirectUri());
        params.add("token_uri", mockGoogleOauthConfig.getTokenUri());
        params.add("resource_uri", mockGoogleOauthConfig.getResourceUri());

        // Mockito를 사용하여 fetchToken 및 fetchMember 메서드의 반환값 설정
        when(mockGoogleApiClient.fetchToken(params)).thenReturn(mockToken);
        when(mockGoogleApiClient.fetchMember(mockToken.access_token())).thenReturn(mockResponse);

        // When
        OauthMember oauthMember = clientComposite.fetch(oauthServerType, mockAuthCode);

        // Then
        assertEquals("12345", oauthMember.getOauthId().getOauthServerId());
        assertEquals("test@gmail.com", oauthMember.getEmail());
        assertEquals("John Doe", oauthMember.getNickname());
        assertEquals("https://example.com/picture", oauthMember.getProfileImageUrl());
    }
}
