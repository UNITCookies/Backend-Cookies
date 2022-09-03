package com.letter.cookies.controller;

import com.letter.cookies.dto.member.request.TempLoginRequest;
import com.letter.cookies.dto.member.response.TempLoginResponse;
import com.letter.cookies.dto.response.CustomResponse;
import com.letter.cookies.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.letter.cookies.dto.response.CustomResponseStatus.SUCCESS;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<CustomResponse> createUser(@RequestBody @Valid TempLoginRequest tempLoginRequest){
        TempLoginResponse tempLoginResponse = memberService.createMember(tempLoginRequest);
        return new CustomResponse<>(tempLoginResponse, SUCCESS).toResponseEntity();
    }
}
