package com.letter.cookies.dto.member.response;

import com.letter.cookies.domain.base.Member.Member;
import com.letter.cookies.dto.member.request.TempLoginRequest;
import lombok.*;

@NoArgsConstructor(force = true)
@Builder
@Getter
@AllArgsConstructor
public class TempLoginResponse {
    private final String memberId;
}
