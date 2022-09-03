package com.letter.cookies.domain.base.Letter;

import com.letter.cookies.domain.base.BaseEntity;
import com.letter.cookies.domain.base.Member.Member;
import lombok.AllArgsConstructor;
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

    private Float x;

    private Float y;

    private Long enableCount;

    public void biteEaten() {
        this.enableCount--;
    }

}
