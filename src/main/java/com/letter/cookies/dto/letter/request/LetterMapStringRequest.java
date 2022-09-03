package com.letter.cookies.dto.letter.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LetterMapStringRequest {

    private String curMemberX;

    private String curMemberY;

    private String startX;

    private String startY;

    private String endX;

    private String endY;

    public LetterMapRequest toMapRequest() {
        return LetterMapRequest.builder()
                .curMemberX(Double.parseDouble(curMemberX))
                .curMemberY(Double.parseDouble(curMemberY))
                .startX(Double.parseDouble(startX))
                .startY(Double.parseDouble(startY))
                .endX(Double.parseDouble(endX))
                .endY(Double.parseDouble(endY))
                .build();
    }

}
