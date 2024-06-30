package dev.backend.wakuwaku.global.infra.oauth.google.client;

import dev.backend.wakuwaku.global.exception.ExceptionStatus;
import dev.backend.wakuwaku.global.infra.oauth.google.dto.GoogleMemberResponse;
import dev.backend.wakuwaku.global.infra.oauth.google.dto.GoogleToken;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class GoogleApiClientImpl implements GoogleApiClient {

    private final RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(GoogleApiClientImpl.class);

    public GoogleApiClientImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static final String GOOGLE_API_TOKEN_URL = "https://oauth2.googleapis.com/token";


    @Override
    public GoogleToken fetchToken(MultiValueMap<String, String> params) {
        GoogleToken token = restTemplate.postForObject(GOOGLE_API_TOKEN_URL, params, GoogleToken.class);
        logger.info("Fetched Token: {}", token);
        return token;
    }

    @Override
    public GoogleMemberResponse fetchMember(String accessToken) {
        logger.info("엑세스 토큰 : {}", accessToken);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        logger.info("헤더 : {}", headers);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<GoogleMemberResponse> response = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v2/userinfo",
                    HttpMethod.GET,
                    entity,
                    GoogleMemberResponse.class
            );
            logger.info("Response: {}", response.getBody());
            return response.getBody();
        } catch (HttpClientErrorException e) {
            logger.error("HTTP Client 오류: {}", e.getResponseBodyAsString(), e);
            throw e;
        } catch (Exception e) {
            throw new  RuntimeException(ExceptionStatus.NOT_EXISTED_MEMBER_INFO.getMessage());
        }
    }

}
