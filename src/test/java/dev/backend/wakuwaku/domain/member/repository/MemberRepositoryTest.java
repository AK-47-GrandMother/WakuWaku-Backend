package dev.backend.wakuwaku.domain.member.repository;

import dev.backend.wakuwaku.domain.member.entity.Member;
import dev.backend.wakuwaku.domain.oauth.dto.OauthServerType;
import dev.backend.wakuwaku.domain.oauth.dto.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    private Member savedMember;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .oauthServerId("testOauthServerId")
                .oauthServerType(OauthServerType.GOOGLE)
                .email("test@example.com")
                .birthday("1990-01-01")
                .nickname("testNickname")
                .profileImageUrl("http://example.com/image.jpg")
                .role(Role.USER)
                .build();

        savedMember = memberRepository.save(member);
    }

    @DisplayName("ID로 Member 조회")
    @Test
    void findById() {
        // given
        Long memberId = savedMember.getId();

        // when
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        // then
        assertThat(optionalMember).isPresent();
        Member foundMember = optionalMember.get();
        assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
        assertThat(foundMember.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(foundMember.getNickname()).isEqualTo(savedMember.getNickname());
        assertThat(foundMember.getProfileImageUrl()).isEqualTo(savedMember.getProfileImageUrl());
        assertThat(foundMember.getBirthday()).isEqualTo(savedMember.getBirthday());
        assertThat(foundMember.getOauthServerId()).isEqualTo(savedMember.getOauthServerId());
        assertThat(foundMember.getOauthServerType()).isEqualTo(savedMember.getOauthServerType());
        assertThat(foundMember.getRole()).isEqualTo(savedMember.getRole());
    }

    @DisplayName("이메일로 Member 조회")
    @Test
    void findByEmail() {
        // given
        String email = savedMember.getEmail();

        // when
        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        // then
        assertThat(optionalMember).isPresent();
        Member foundMember = optionalMember.get();
        assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
        assertThat(foundMember.getEmail()).isEqualTo(savedMember.getEmail());
        assertThat(foundMember.getNickname()).isEqualTo(savedMember.getNickname());
        assertThat(foundMember.getProfileImageUrl()).isEqualTo(savedMember.getProfileImageUrl());
        assertThat(foundMember.getBirthday()).isEqualTo(savedMember.getBirthday());
        assertThat(foundMember.getOauthServerId()).isEqualTo(savedMember.getOauthServerId());
        assertThat(foundMember.getOauthServerType()).isEqualTo(savedMember.getOauthServerType());
        assertThat(foundMember.getRole()).isEqualTo(savedMember.getRole());
    }
}
