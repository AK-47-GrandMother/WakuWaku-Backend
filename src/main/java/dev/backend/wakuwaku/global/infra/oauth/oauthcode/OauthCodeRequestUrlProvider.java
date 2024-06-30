package dev.backend.wakuwaku.global.infra.oauth.oauthcode;

import dev.backend.wakuwaku.domain.oauth.dto.OauthServerType;

/**
 *
 * 이는 인터페이스로, AuthCode를 발급할 URL을 제공하는 기능을 제공합니다.
 * supportServer()는 자신이 어떤 OauthServerType를 지원할 수 있는지를 나타합니다.
 * 예를 들어 KakaoAuthCodeRequestUrlProvider는 OauthServerType으로 KAKAO를 반환할 것입니다.
 * provide()를 통해 URL을 생성하여 반환하며, 해당 주소로 Redirect 한다면 다음과 같은 화면이 나옵니다.
 */
public interface OauthCodeRequestUrlProvider {

    OauthServerType supportServer();

    String provide();
}