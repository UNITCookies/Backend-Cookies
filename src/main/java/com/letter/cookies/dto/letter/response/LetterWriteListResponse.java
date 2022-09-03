package com.letter.cookies.dto.letter.response;

import com.letter.cookies.domain.base.Letter.Letter;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class LetterWriteListResponse {

    private Long letterId;
    private String writerNickname;
    private String letterContent;
    private Double x;
    private Double y;
    private Long enableCount;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    @Builder
    public LetterWriteListResponse(Letter letter) {
        this.letterId = letter.getLetterId();
        this.writerNickname = letter.getWriterNickname();
        this.letterContent = letter.getLetterContent();
        this.x = letter.getXAxis();
        this.y = letter.getYAxis();
        this.enableCount = letter.getEnableCount();
        this.createdAt = letter.getCreatedAt();
        this.updatedAt = letter.getUpdatedAt();
    }
}
