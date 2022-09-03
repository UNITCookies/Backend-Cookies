package com.letter.cookies.dto.member.response;

import lombok.*;

@NoArgsConstructor(force = true)
@Builder
@Getter
@AllArgsConstructor
public class TempLoginResponse {
    private final String memberId;
}
