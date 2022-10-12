package com.letter.cookies.dto.member.request;

import com.letter.cookies.domain.base.Member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
public class LoginRequest {

    @NotBlank
    @Size(min=3, max=10)
    private String memberName;

    @NotBlank
    @Email
    private String memberEmail;

    private String memberPassword;

    @NotBlank
    private String identifier;

    public Member toEntity(Long cookie){
        return Member.builder()
                .memberName(memberName)
                .memberPassword(memberPassword)
                .memberEmail(memberEmail)
                .identifier(identifier)
                .cookie(cookie)
                .build();
    }

}
