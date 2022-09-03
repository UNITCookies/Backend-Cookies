package com.letter.cookies.dto.letter.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.letter.cookies.domain.base.Letter.Letter;
import com.letter.cookies.domain.base.ReadLetter.ReadLetter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(force = true)
public class LetterReadListResponse {
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
    public LetterReadListResponse(Letter letter) {
        this.letterId = letter.getLetterId();
        this.writerNickname = letter.getWriterNickname();
        this.letterContent = letter.getLetterContent();
        this.x = letter.getX();
        this.y = letter.getY();
        this.enableCount = letter.getEnableCount();
        this.createdAt = letter.getCreatedAt().toLocalDate();
        this.updatedAt = letter.getUpdatedAt().toLocalDate();
    }

    public static List<LetterReadListResponse> listOf(List<Letter> letterList) {
        return letterList.stream().map(letter -> LetterReadListResponse.builder()
                .letter(letter).build()).collect(Collectors.toList());
    }
}
