package dev.backend.wakuwaku.global.infra.oauth.google.dto;

import dev.backend.wakuwaku.domain.oauth.dto.OauthId;
import dev.backend.wakuwaku.domain.oauth.dto.OauthMember;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static dev.backend.wakuwaku.domain.oauth.dto.OauthServerType.GOOGLE;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GoogleMemberResponse {
    String id;
    String email;
    String given_name;
    String family_name;
    String picture;

    public OauthMember toDomain() {
        return OauthMember.builder()
                .oauthId(new OauthId(String.valueOf(id), GOOGLE))
                .email(email)
                .nickname(given_name + " " + family_name)
                .profileImageUrl(picture)
                .build();
    }
}
