package com.letter.cookies.dto.member.request;

import com.letter.cookies.domain.base.Member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor(force = true)
public class TempLoginRequest {

    @NotBlank
    @Size(min=3, max=10)
    private final String memberName;
    @NotBlank
    @Size(min=3, max=20)
    private final String memberPassword;

    public Member toEntity(TempLoginRequest tempLoginRequest){
        return Member.builder().memberName(tempLoginRequest.getMemberName())
                .memberPassword(tempLoginRequest.getMemberPassword()).build();
    }
}
