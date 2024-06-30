package dev.backend.wakuwaku.global.infra.oauth.presentation;

import dev.backend.wakuwaku.domain.oauth.dto.OauthServerType;
import org.springframework.core.convert.converter.Converter;

/**
 * String을 OauthServerType으로 변환해줍니다.
 * 그리고 이를 적용하기 위해서는 다음과 같이 WebConfigurer를 구현하여 추가해주어야 합니다.
 */

public class OauthServerTypeConverter implements Converter<String, OauthServerType> {

    @Override
    public OauthServerType convert(String source) {
        return OauthServerType.fromName(source);
    }
}
