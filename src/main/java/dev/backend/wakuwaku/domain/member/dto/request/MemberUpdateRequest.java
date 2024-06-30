package dev.backend.wakuwaku.domain.member.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateRequest {

    private String nickname;
    private String profileImageUrl;
    private String birthday;

    public MemberUpdateRequest(String nickname, String profileImageUrl, String birthday)
    {
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.birthday = birthday;
    }


}
