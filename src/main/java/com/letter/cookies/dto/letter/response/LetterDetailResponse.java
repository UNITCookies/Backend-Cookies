package com.letter.cookies.dto.letter.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class LetterDetailResponse {
    private final String letterContent;
    private final String letterNickname;

    @Builder
    public LetterDetailResponse(String letterContent, String letterNickname){
        this.letterContent = letterContent;
        this.letterNickname = letterNickname;
    }
}
