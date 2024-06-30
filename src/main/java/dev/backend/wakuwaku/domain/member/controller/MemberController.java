package dev.backend.wakuwaku.domain.member.controller;


import dev.backend.wakuwaku.domain.member.dto.request.MemberUpdateRequest;
import dev.backend.wakuwaku.domain.member.dto.response.GetMemberResponse;
import dev.backend.wakuwaku.domain.member.dto.response.MemberIdResponse;
import dev.backend.wakuwaku.domain.member.entity.Member;
import dev.backend.wakuwaku.domain.member.service.MemberService;
import dev.backend.wakuwaku.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("wakuwaku/v1/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService;


    @GetMapping("/{id}")
    public BaseResponse<GetMemberResponse> findById(@PathVariable("id") Long id) {
        Member memberEntity = memberService.findById(id);
        
        return new BaseResponse<>(new GetMemberResponse(memberEntity));

    }

    @PutMapping("/{id}")
    public BaseResponse<MemberIdResponse> update(@PathVariable("id") Long id, @RequestBody MemberUpdateRequest memberUpdateRequest) {
        Long dbId = memberService.update(id, memberUpdateRequest);

        return new BaseResponse<>(new MemberIdResponse(dbId));

    }
    @DeleteMapping("/{id}") // 삭제
    public BaseResponse<Void> delete(@PathVariable("id") Long id) {
        memberService.deactivateById(id);

        return new BaseResponse<>();
    }

    @GetMapping("/list")
    public BaseResponse<List<GetMemberResponse>> findAll() {
        List<Member> memberList = memberService.findAll();

        return new BaseResponse<>(memberList.stream()
                .map(GetMemberResponse::new)
                .toList());
    }

}
