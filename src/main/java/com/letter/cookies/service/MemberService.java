package com.letter.cookies.service;

import com.letter.cookies.domain.base.Member.Member;
import com.letter.cookies.domain.base.Member.MemberRepository;
import com.letter.cookies.dto.member.request.LoginRequest;
import com.letter.cookies.dto.member.request.TempLoginRequest;
import com.letter.cookies.dto.member.response.MemberInfoResponse;
import com.letter.cookies.dto.member.response.TempLoginResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public TempLoginResponse createMember(TempLoginRequest tempLoginRequest) {
        Member member = memberRepository
                .save(tempLoginRequest.toEntity(tempLoginRequest));
        return TempLoginResponse.builder().memberId(member.getMemberId().toString()).build();
    }

    @Transactional
    public MemberInfoResponse login(LoginRequest loginRequest) {
        Member member = memberRepository.findByIdentifier(loginRequest.getIdentifier());
        if (member == null) { // 신규유저
            member = loginRequest.toEntity(10L);
            memberRepository.save(member);
        } else { // 기존유저
            member.update(loginRequest.getMemberName());
        }
        return new MemberInfoResponse(member);
    }

    public MemberInfoResponse getMemberInfo(String identifier) {
        Member member = memberRepository.findByIdentifier(identifier);
        return new MemberInfoResponse(member);
    }

    // 매일 정각 memberWriteCountPerDay 0으로 업데이트
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void initMemberWriteLetterCount() {
        List<Member> memberList = memberRepository.findAll();
        memberList.forEach(member -> {
            member.updateWriteCountPerDay(0);
        });
        memberRepository.saveAll(memberList);
    }
}
