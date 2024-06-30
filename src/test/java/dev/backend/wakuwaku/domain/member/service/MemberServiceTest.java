package dev.backend.wakuwaku.domain.member.service;

import dev.backend.wakuwaku.domain.member.dto.request.MemberUpdateRequest;
import dev.backend.wakuwaku.domain.member.entity.Member;
import dev.backend.wakuwaku.domain.member.repository.MemberRepository;
import dev.backend.wakuwaku.global.exception.ExceptionStatus;
import dev.backend.wakuwaku.global.exception.WakuWakuException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .email("test@example.com")
                .nickname("testUser")
                .profileImageUrl("https://example.com/profile.jpg")
                .birthday("1990-01-01")
                .build();
    }

    @Test
    @DisplayName("중복되는 이메일이 존재하는 경우 - 실패")
    void validateDuplicateMember_DuplicateEmail() {
        // given
        String email = "test@example.com";
        Member existingMember = new Member();
        existingMember.createEmail(email);
        existingMember.createCheckstatus("Y"); // "Y"로 설정하여 중복 상황을 시뮬레이트
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(existingMember));

        // when & then
        assertThatThrownBy(() -> memberService.validateDuplicateMember(existingMember))
                .isInstanceOf(WakuWakuException.class)
                .extracting("status")
                .isEqualTo(ExceptionStatus.DUPLICATED_EMAIL);

        // Optional: memberRepository의 findByEmail 메서드가 한 번 호출되었는지 확인
        // 실제 로직에서는 메서드가 호출됨을 보장하기 위해 추가적인 검증 가능
    }

    @Test
    @DisplayName("중복되는 이메일이 존재하지 않는 경우 - 성공")
    void validateDuplicateMember_NonDuplicateEmail() {
        // given
        String email = "test@example.com";
        Member newMember = new Member();
        newMember.createEmail(email);
        newMember.createCheckstatus("N"); // "N"으로 설정하여 중복 상황을 회피
        given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

        // when & then
        memberService.validateDuplicateMember(newMember); // 예외가 발생하지 않아야 함

        // Optional: memberRepository의 findByEmail 메서드가 한 번 호출되었는지 확인
        // 실제 로직에서는 메서드가 호출됨을 보장하기 위해 추가적인 검증 가능
    }
    @Test
    @DisplayName("모든 회원 조회 - 성공")
    void findAll() {
        // given
        given(memberRepository.findAll()).willReturn(List.of(member));

        // when
        List<Member> allMembers = memberService.findAll();

        // then
        assertThat(allMembers).hasSize(1);
        assertThat(allMembers.get(0)).isEqualTo(member);
        then(memberRepository).should().findAll();
    }

    @Test
    @DisplayName("ID로 회원 조회 - 성공")
    void findById() {
        // given
        member.createId(1L);
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        // when
        Member foundMember = memberService.findById(1L);

        // then
        assertThat(foundMember).isEqualTo(member);
        then(memberRepository).should().findById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 회원 조회할 때 예외 발생")
    void findByIdNonExistingId() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.findById(1L))
                .isInstanceOf(WakuWakuException.class)
                .extracting("status")
                .isEqualTo(ExceptionStatus.NONE_USER);
        then(memberRepository).should().findById(1L);
    }

    @Test
    @DisplayName("회원 정보 수정 - 성공")
    void update() {
        // given
        MemberUpdateRequest updateRequest = new MemberUpdateRequest("newNickname", "newProfileUrl", "1991-01-01");

        member.createId(1L);
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        // Configure save method to update the member and return it
        given(memberRepository.save(any(Member.class)))
                .willAnswer(invocation -> {
                    Member updatedMember = invocation.getArgument(0);
                    return updatedMember;
                });

        // when
        Long updatedMemberId = memberService.update(1L, updateRequest);

        // then
        assertThat(updatedMemberId).isEqualTo(1L);
        assertThat(member.getNickname()).isEqualTo(updateRequest.getNickname());
        assertThat(member.getProfileImageUrl()).isEqualTo(updateRequest.getProfileImageUrl());
        assertThat(member.getBirthday()).isEqualTo(updateRequest.getBirthday());
        then(memberRepository).should().findById(1L);
        then(memberRepository).should().save(any(Member.class)); // Verify save method called with any Member
    }

    @Test
    @DisplayName("존재하지 않는 ID로 회원 정보 수정할 때 예외 발생")
    void updateNonExistingMember() {
        // given
        MemberUpdateRequest updateRequest = new MemberUpdateRequest("newNickname", "newProfileUrl", "1991-01-01");
        given(memberRepository.findById(1L)).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> memberService.update(1L, updateRequest))
                .isInstanceOf(WakuWakuException.class)
                .hasFieldOrPropertyWithValue("status", ExceptionStatus.NONE_USER);

        then(memberRepository).should().findById(1L);
        then(memberRepository).should(never()).save(any(Member.class));
    }

    @Test
    @DisplayName("회원 비활성화 - 성공")
    void deactivateById() {
        // given
        Long memberId = 1L;
        Member member = new Member();
        member.createId(memberId);
        member.createCheckstatus("Y");

        given(memberRepository.findById(memberId)).willReturn(Optional.of(member));

        // when
        memberService.deactivateById(memberId);

        // then
        then(memberRepository).should().findById(memberId);
        assertThat(member.getCheckStatus()).isEqualTo("N");
        then(memberRepository).should().save(member);
    }

}
