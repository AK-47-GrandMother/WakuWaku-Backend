package dev.backend.wakuwaku.domain.oauth.service;

import dev.backend.wakuwaku.domain.member.entity.Member;
import dev.backend.wakuwaku.domain.member.repository.MemberRepository;
import dev.backend.wakuwaku.domain.oauth.dto.OauthId;
import dev.backend.wakuwaku.domain.oauth.dto.OauthMember;
import dev.backend.wakuwaku.domain.oauth.dto.OauthServerType;
import dev.backend.wakuwaku.global.exception.ExceptionStatus;
import dev.backend.wakuwaku.global.exception.WakuWakuException;
import dev.backend.wakuwaku.global.infra.oauth.client.OauthMemberClientComposite;
import dev.backend.wakuwaku.global.infra.oauth.oauthcode.OauthCodeRequestUrlProviderComposite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class OauthServiceTest {

    @Mock
    private OauthCodeRequestUrlProviderComposite oauthCodeRequestUrlProviderComposite;

    @Mock
    private OauthMemberClientComposite oauthMemberClientComposite;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private OauthService oauthService;

    private static final String AUTH_CODE = "test_auth_code";
    private static final String EMAIL = "test@example.com";
    private static final String NICKNAME = "TestUser";
    private static final String PROFILE_IMAGE_URL = "https://example.com/profile.jpg";
    private static final String BIRTHDAY = "1990-01-01";

    @BeforeEach
    void setUp() {
    }

    @Test
    @DisplayName("로그인 성공 시 회원 정보 반환")
    void loginSuccess() {
        // Given
        OauthServerType oauthServerType = OauthServerType.KAKAO;
        OauthId oauthId = new OauthId("test_oauth_server_id", oauthServerType);
        OauthMember oauthMember = new OauthMember(oauthId, NICKNAME, PROFILE_IMAGE_URL, EMAIL, BIRTHDAY);
        Member existingMember = new Member();
        existingMember.createEmail(EMAIL);
        existingMember.createNickname(NICKNAME);
        existingMember.createProfileImageUrl(PROFILE_IMAGE_URL);
        existingMember.createtBirthday(BIRTHDAY);

        given(oauthMemberClientComposite.fetch(eq(oauthServerType), eq(AUTH_CODE))).willReturn(oauthMember);
        given(memberRepository.findByEmail(eq(EMAIL))).willReturn(Optional.empty());
        given(memberRepository.save(any(Member.class))).willReturn(existingMember);

        // When
        Map<String, Long> response = oauthService.login(oauthServerType, AUTH_CODE);

        // Then
        assertThat(response).isNotNull();
        assertThat(response).containsKeys("id");
        then(memberRepository).should().save(any(Member.class));
    }



    @Test
    @DisplayName("이미 존재하는 회원인 경우 기존 회원 정보 반환")
    void loginExistingMember() {
        // Given
        OauthServerType oauthServerType = OauthServerType.KAKAO;
        OauthId oauthId = new OauthId("dummy_oauth_server_id", oauthServerType);
        OauthMember oauthMember = new OauthMember(oauthId, NICKNAME, PROFILE_IMAGE_URL, EMAIL, BIRTHDAY);
        Member existingMember = new Member();
        existingMember.createId(1L); // 기존 회원 정보에 ID를 설정하여 가정
        existingMember.createCheckstatus("N"); // 기존 회원의 상태를 "N"으로 설정하여 탈퇴한 상태를 가정

        given(oauthMemberClientComposite.fetch(eq(oauthServerType), eq(AUTH_CODE))).willReturn(oauthMember);
        given(memberRepository.findByEmail(eq(EMAIL))).willReturn(Optional.of(existingMember)); // 기존 회원을 반환하도록 변경

        // When
        Map<String, Long> response = oauthService.login(oauthServerType, AUTH_CODE);

        // Then
        assertThat(response).isNotNull();
        assertThat(response).containsKeys("id");
        assertThat(response.get("id")).isEqualTo(existingMember.getId()); // 기존 회원의 ID가 반환되는지 확인
        assertThat(existingMember.getCheckStatus()).isEqualTo("Y"); // 회원의 상태가 "Y"로 변경되었는지 확인
        then(memberRepository).should().save(existingMember); // 상태 변경 후 저장이 호출되었는지 확인
    }





    @Test
    @DisplayName("로그인 실패 시 예외 발생")
    void loginFailure() {
        // Given
        OauthServerType oauthServerType = OauthServerType.KAKAO;

        given(oauthMemberClientComposite.fetch(eq(oauthServerType), eq(AUTH_CODE))).willThrow(new RuntimeException("OAuth client error"));

        // When / Then
        assertThatThrownBy(() -> oauthService.login(oauthServerType, AUTH_CODE))
                .isInstanceOf(WakuWakuException.class)
                .hasFieldOrPropertyWithValue("status", ExceptionStatus.FALIED_TO_LOGIN);
    }
}
