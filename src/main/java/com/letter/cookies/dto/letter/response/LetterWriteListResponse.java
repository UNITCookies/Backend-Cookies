package com.letter.cookies.dto.letter.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.letter.cookies.domain.base.Letter.Letter;
import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
public class LetterWriteListResponse {

    private Long letterId;
    private String writerNickname;
    private String letterContent;
    private Double x;
    private Double y;
    private Long enableCount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate updatedAt;

    @Builder
    public LetterWriteListResponse(Letter letter) {
        this.letterId = letter.getLetterId();
        this.writerNickname = letter.getWriterNickname();
        this.letterContent = letter.getLetterContent();
        this.x = letter.getX();
        this.y = letter.getY();
        this.enableCount = letter.getEnableCount();
        this.createdAt = letter.getCreatedAt().toLocalDate();
        this.updatedAt = letter.getUpdatedAt().toLocalDate();
    }
}
