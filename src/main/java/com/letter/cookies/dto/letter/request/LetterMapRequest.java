package com.letter.cookies.dto.letter.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
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

    public LetterMapRequest setStartXY() {
        if (endX < startX) {
            Double temp = startX;
            this.startX = endX;
            this.endX = temp;
            log.info("startX: {}", startX);
            log.info("endX: {}", endX);
        }
        if (endY < startY) {
            Double temp = startY;
            this.startY = endY;
            this.endY = temp;
            log.info("startY: {}", startY);
            log.info("endY: {}", endY);
        }

        return this;
    }

}
