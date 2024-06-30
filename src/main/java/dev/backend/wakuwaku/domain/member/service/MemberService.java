package dev.backend.wakuwaku.domain.member.service;

import dev.backend.wakuwaku.domain.member.dto.request.MemberUpdateRequest;
import dev.backend.wakuwaku.domain.member.entity.Member;
import dev.backend.wakuwaku.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.backend.wakuwaku.global.exception.WakuWakuException.DUPLICATED_EMAIL;
import static dev.backend.wakuwaku.global.exception.WakuWakuException.NONE_USER;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    protected void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail())
                .ifPresent(m -> {
                    if (!m.getCheckStatus().equals("N")) {
                        throw DUPLICATED_EMAIL;
                    }
                });
    }

    /*
    회원 리스트
     */
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    /*
    회원 ID 찾기
     */
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(
                        () -> NONE_USER
                );
    }

    /*
    회원 정보 수정
     */
    public Long update(Long id, MemberUpdateRequest memberUpdateRequest) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> NONE_USER); // 예외 처리 추가

        member.updateNickname(memberUpdateRequest.getNickname());
        member.updateProfileImageUrl(memberUpdateRequest.getProfileImageUrl());
        member.updateBirthday(memberUpdateRequest.getBirthday());

        memberRepository.save(member);

        return member.getId();
    }


    /*
    회원 탈퇴
     */
    public void deactivateById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> NONE_USER);
        // checkStatus : Y -> N
        member.deactivate();
        memberRepository.save(member);
    }
}
