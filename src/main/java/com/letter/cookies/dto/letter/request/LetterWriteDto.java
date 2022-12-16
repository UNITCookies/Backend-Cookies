package com.letter.cookies.dto.letter.request;

import com.letter.cookies.domain.base.Letter.Letter;
import com.letter.cookies.domain.base.Member.Member;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterWriteDto {

    @Setter
    private String writerNickname;

    @Setter
    private String letterTitle;

    private String letterContent;

//    private Double x;
//    private Double y;
    private String x;

    private String y;

    private long enableCount;

    public Letter toEntity(Member member) {

        return Letter.builder()
                .member(member)
                .writerNickname(this.writerNickname)
                .letterTitle(letterTitle)
                .letterContent(this.letterContent)
                .x(Double.valueOf(this.x))
                .y(Double.valueOf(this.y))
                .enableCount(4L)
                .build();
    }
}