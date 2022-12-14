package com.letter.cookies.dto.letter.response;

import com.letter.cookies.domain.base.Letter.Letter;
import com.letter.cookies.domain.base.Member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class LetterWriteResponse {

    private Long letterId;
    private String writerNickname;
    private String letterTitle;
    private String letterContent;
    private Double x;
    private Double y;
    private Long enableCount;
    private Long cookie;

    @Builder
    public LetterWriteResponse(Letter letter, Member member) {
        this.letterId = letter.getLetterId();
        this.letterTitle = letter.getLetterTitle();
        this.writerNickname = letter.getWriterNickname();
        this.letterContent = letter.getLetterContent();
        this.x = letter.getX();
        this.y = letter.getY();
        this.enableCount = letter.getEnableCount();
        this.cookie = member.getCookie();
    }
}
