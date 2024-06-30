package dev.backend.wakuwaku.global.infra.oauth.config;

import dev.backend.wakuwaku.global.infra.oauth.kakao.client.KakaoApiClient;
import dev.backend.wakuwaku.global.infra.oauth.naver.client.NaverApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

/**
 * kakaoApiClient(): KakaoApiClient 클래스의 빈을 생성합니다. Kakao API와 통신하기 위한 클라이언트
 * naverApiClient(): NaverApiClient 클래스의 빈을 생성합니다. Naver API와 통신하기 위한 클라이언트
 */
@Configuration
public class HttpInterfaceConfig {

    @Bean
    public KakaoApiClient kakaoApiClient() {
        return createHttpInterface(KakaoApiClient.class);
    }

    @Bean
    public NaverApiClient naverApiClient() {
        return createHttpInterface(NaverApiClient.class);
    }

    private <T> T createHttpInterface(Class<T> clazz) {
        WebClient webClient = WebClient.create();
        HttpServiceProxyFactory build = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient)).build();
        return build.createClient(clazz);
    }
}