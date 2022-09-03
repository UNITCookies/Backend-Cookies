package com.letter.cookies.dto.letter.request;

import com.letter.cookies.domain.base.Letter.Letter;
import com.letter.cookies.domain.base.Member.Member;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LetterWriteDto {

    @Setter
    private String writerNickname;

    private String letterContent;

    private Double x;

    private Double y;

    private Long enableCount;

    public Letter toEntity(Member member) {

        return Letter.builder()
                .member(member)
                .writerNickname(this.writerNickname)
                .letterContent(this.letterContent)
                .xAxis(this.x)
                .yAxis(this.y)
                .enableCount(4L)
                .build();
    }
}
