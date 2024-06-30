package dev.backend.wakuwaku.domain.oauth.controller;

import dev.backend.wakuwaku.domain.oauth.dto.OauthServerType;
import dev.backend.wakuwaku.domain.oauth.service.OauthService;
import dev.backend.wakuwaku.global.response.BaseResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class OauthController {

    private final OauthService oauthService;

    @GetMapping("/{oauthServerType}")
    BaseResponse<Void> redirectAuthCodeRequestUrl(
            @PathVariable(name = "oauthServerType") String oauthServerTypeStr,
            HttpServletResponse response
    ) throws IOException {

        OauthServerType oauthServerType = OauthServerType.fromName(oauthServerTypeStr);
        if (oauthServerType == null) {
            return new BaseResponse<>();
        }

        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        if ("google".equals(oauthServerTypeStr)) {
            // 스코프 파라미터를 추가하기 전에 이전에 포함된 스코프 파라미터를 제거
            redirectUrl = redirectUrl.replaceAll("&scope=[^&]*", "");
            redirectUrl += "&scope=profile%20email%20profile%20openid&access_type=offline";
            // 스페이스를 URL 인코딩하여 추가
        }

        response.sendRedirect(redirectUrl);

        return new BaseResponse<>();
    }

    @GetMapping("/login/{oauthServerType}")
    BaseResponse<Map<String, Long>> login(
            @PathVariable(name = "oauthServerType") OauthServerType oauthServerType,
            @RequestParam(name = "code") String code) {

        Map<String, Long> login = oauthService.login(oauthServerType, code);
        return new BaseResponse<>(login);
    }
}

