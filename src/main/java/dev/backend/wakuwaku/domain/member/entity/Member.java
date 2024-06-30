package dev.backend.wakuwaku.domain.member.entity;

import dev.backend.wakuwaku.domain.StatusEntity;
import dev.backend.wakuwaku.domain.oauth.dto.OauthServerType;
import dev.backend.wakuwaku.domain.oauth.dto.Role;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "member_table",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "oauth_id_unique",
                        columnNames = {
                                "oauth_server_id",
                                "oauth_server"
                        }
                )
        }
)
public class Member extends StatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "oauth_server_id")
    private String oauthServerId;

    @Enumerated(STRING)
    @Column(nullable = false, name = "oauth_server")
    private OauthServerType oauthServerType;

    @Column
    private String birthday;

    @Column
    private String email;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(length = 200)
    private String profileImageUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String oauthServerId, OauthServerType oauthServerType, String email,
                  String birthday, String nickname, String profileImageUrl, Role role) {
        this.oauthServerId = oauthServerId;
        this.oauthServerType = oauthServerType;
        this.email = email;
        this.birthday = birthday;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.role = role;
        this.checkStatus = "Y"; // 회원 가입 시 기본값 설정
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    public void updateBirthday(String birthday) {
        this.birthday = birthday;
    }
    public void updateCheckstatus(String checkStatus) {this.checkStatus = checkStatus;  }

    public void deactivate() {
        this.checkStatus = "N";
    }


    /**
     * 테스트 코드에서 사용 (setter)
     */
    public void createEmail(String email) {
        this.email = email;
    }
    public void createCheckstatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }
    public void createNickname(String nickname) {
        this.nickname = nickname;
    }
    public void createProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
    public void createtBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void createId(Long id) {
        this.id = id;
    }

}
