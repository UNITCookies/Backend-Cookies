package com.letter.cookies.domain.base.Letter;

import com.letter.cookies.domain.base.BaseEntity;
import com.letter.cookies.domain.base.Member.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Letter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long letterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private String writerNickname;

    private String letterContent;

    private Double x;

    private Double y;

    private Long enableCount;

    @Builder
    public Letter(Member member, String writerNickname, String letterContent, Double x, Double y, Long enableCount) {
        this.member = member;
        this.writerNickname = writerNickname;
        this.letterContent = letterContent;
        this.x = x;
        this.y = y;
        this.enableCount = enableCount;

    }

    public void biteEaten() {
        this.enableCount--;
    }

}
