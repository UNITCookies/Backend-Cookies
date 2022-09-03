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

    @Column(name = "x_axis")
    private Double xAxis;

    @Column(name = "y_axis")
    private Double yAxis;

    private Long enableCount;

    @Builder
    public Letter(Member member, String writerNickname, String letterContent, Double xAxis, Double yAxis, Long enableCount) {
        this.member = member;
        this.writerNickname = writerNickname;
        this.letterContent = letterContent;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.enableCount = enableCount;
    }

}
