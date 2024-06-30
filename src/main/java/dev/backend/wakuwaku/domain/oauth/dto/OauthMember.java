package dev.backend.wakuwaku.domain.oauth.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

//@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "oauth_member",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "oauth_id_unique",
                        columnNames = {
                                "oauth_server_id",
                                "oauth_server"
                        }
                ),
        }
)
public class OauthMember {



    @Embedded
    private OauthId oauthId;
    @Column(nullable = false, length = 50) // 필드의 길이 및 null 허용 여부 설정
    private String nickname;
    @Column(length = 200) // 프로필 이미지 URL의 최대 길이 설정
    private String profileImageUrl;

    @Column
    private String email;

    @Column
    private String birthday;


}