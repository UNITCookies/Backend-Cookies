package com.letter.cookies.dto.member.response;

import com.letter.cookies.domain.base.Member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberInfoResponse {

    private String memberName;
    private Long cookie;

    public MemberInfoResponse(Member entity) {
        this.memberName = entity.getMemberName();
        this.cookie = entity.getCookie();
    }

}
