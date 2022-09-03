package com.letter.cookies.dto.letter.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class LetterMapRequest {

    private UUID memberId;

    private Double curMemberX;

    private Double curMemberY;

    private Double startX;

    private Double startY;

    private Double endX;

    private Double endY;

}
