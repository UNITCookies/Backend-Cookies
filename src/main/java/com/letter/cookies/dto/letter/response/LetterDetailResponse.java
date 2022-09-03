package com.letter.cookies.dto.letter.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class LetterDetailResponse {
    private final String letterContent;
    private final String letterNickname;

    private final double x;
    private final double y;
    private final long enableCount;

    @Builder
    public LetterDetailResponse(String letterContent, String letterNickname,
                                double x, double y, long enableCount){
        this.letterContent = letterContent;
        this.letterNickname = letterNickname;
        this.x = x;
        this.y = y;
        this.enableCount = enableCount;
    }
}
