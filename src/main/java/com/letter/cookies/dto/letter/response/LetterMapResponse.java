package com.letter.cookies.dto.letter.response;

import com.letter.cookies.domain.base.Letter.Letter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@NoArgsConstructor
public class LetterMapResponse {

    private Long letterId;

    private Double x;

    private Double y;

    private Long enableCount;

    public LetterMapResponse(Letter entity) {
        this.letterId = entity.getLetterId();
        this.x = entity.getX();
        this.y = entity.getY();
        this.enableCount = entity.getEnableCount();
    }

}
