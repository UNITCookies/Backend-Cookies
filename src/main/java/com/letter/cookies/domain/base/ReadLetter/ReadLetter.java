package com.letter.cookies.domain.base.ReadLetter;

import com.letter.cookies.domain.base.BaseEntity;
import com.letter.cookies.domain.base.Letter.Letter;
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
public class ReadLetter extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long readLetterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "letter_id")
    private Letter letter;

    @Builder
    public ReadLetter(Member member, Letter letter) {
        setMember(member);
        setLetter(letter);
    }

    private void setMember(Member member) {
        this.member = member;
    }

    private void setLetter(Letter letter) {
        this.letter = letter;
    }
}
