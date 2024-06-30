package dev.backend.wakuwaku.global.infra.oauth.google.client;

import dev.backend.wakuwaku.global.infra.oauth.google.dto.GoogleMemberResponse;
import dev.backend.wakuwaku.global.infra.oauth.google.dto.GoogleToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GoogleApiClientImplTest {

    @Mock
    private RestTemplate restTemplate;

    private GoogleApiClientImpl googleApiClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        googleApiClient = new GoogleApiClientImpl(restTemplate);
    }

    @Test
    @DisplayName("구글 엑세스 토큰 가져오기")
    void testFetchToken() {
        // Mock data
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "mockClientId");
        params.add("client_secret", "mockClientSecret");
        params.add("code", "mockAuthCode");
        params.add("redirect_uri", "mockRedirectUri");
        params.add("token_uri", "mockTokenUri");
        params.add("resource_uri", "mockResourceUri");

        GoogleToken expectedToken = new GoogleToken("mock_access_token", "bearer", 3600, null, null);

        // Mock behavior for postForObject
        when(restTemplate.postForObject(
                ArgumentMatchers.eq(GoogleApiClientImpl.GOOGLE_API_TOKEN_URL),
                ArgumentMatchers.eq(params),
                ArgumentMatchers.eq(GoogleToken.class)
        )).thenReturn(expectedToken);

        // Perform fetchToken operation
        GoogleToken token = googleApiClient.fetchToken(params);

        // Verify interactions and assertions
        verify(restTemplate, times(1)).postForObject(
                ArgumentMatchers.eq(GoogleApiClientImpl.GOOGLE_API_TOKEN_URL),
                ArgumentMatchers.eq(params),
                ArgumentMatchers.eq(GoogleToken.class)
        );
        assertEquals(expectedToken.access_token(), token.access_token());
        assertEquals(expectedToken.token_type(), token.token_type());
        assertEquals(expectedToken.expires_in(), token.expires_in());
    }


    @Test
    @DisplayName("구글 사용자 정보 가져오기")
    void testFetchMember() {
        // 테스트 데이터
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("mockAccessToken");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<GoogleMemberResponse> responseEntity = new ResponseEntity<>(new GoogleMemberResponse("123", "test@example.com", "John", "Doe", "https://example.com/pic.jpg"), HttpStatus.OK);


        when(restTemplate.exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(HttpMethod.GET),
                ArgumentMatchers.<HttpEntity<?>>any(),
                ArgumentMatchers.eq(GoogleMemberResponse.class)
        )).thenReturn(responseEntity);

        // 엑세스 토큰 값을 통해 사용자 정보 가져옴.
        GoogleMemberResponse response = googleApiClient.fetchMember("mockAccessToken");


        verify(restTemplate, times(1)).exchange(
                ArgumentMatchers.anyString(),
                ArgumentMatchers.eq(HttpMethod.GET),
                ArgumentMatchers.<HttpEntity<?>>any(),
                ArgumentMatchers.eq(GoogleMemberResponse.class)
        );

        // 검증
        assertEquals("123", response.getId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("John", response.getGiven_name());
        assertEquals("Doe", response.getFamily_name());
        assertEquals("https://example.com/pic.jpg", response.getPicture());
    }

}
