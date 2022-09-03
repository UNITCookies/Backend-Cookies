package com.letter.cookies.dto.letter.response;

import com.letter.cookies.domain.base.Letter.Letter;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LetterWriteResponse {

    private Long letterId;
    private String writerNickname;
    private String letterContent;
    private Double x;
    private Double y;
    private Long enableCount;

    @Builder
    public LetterWriteResponse(Letter letter) {
        this.letterId = letter.getLetterId();
        this.writerNickname = letter.getWriterNickname();
        this.letterContent = letter.getLetterContent();
        this.x = letter.getX();
        this.y = letter.getY();
        this.enableCount = letter.getEnableCount();
    }
}
