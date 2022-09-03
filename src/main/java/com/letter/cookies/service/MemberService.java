package com.letter.cookies.service;

import com.letter.cookies.domain.base.Member.Member;
import com.letter.cookies.domain.base.Member.MemberRepository;
import com.letter.cookies.dto.member.request.TempLoginRequest;
import com.letter.cookies.dto.member.response.TempLoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public TempLoginResponse createMember(TempLoginRequest tempLoginRequest){
        Member member = memberRepository
                .save(tempLoginRequest.toEntity(tempLoginRequest));
        return TempLoginResponse.builder().memberId(member.getMemberId().toString()).build();
    }
}
