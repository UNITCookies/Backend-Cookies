package com.letter.cookies.dto.letter.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class LetterMapRequest {

    private Double curMemberX;

    private Double curMemberY;

    private Double startX;

    private Double startY;

    private Double endX;

    private Double endY;

    @Builder
    public LetterMapRequest(Double curMemberX, Double curMemberY, Double startX, Double startY, Double endX, Double endY) {
        this.curMemberX = curMemberX;
        this.curMemberY = curMemberY;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

}
