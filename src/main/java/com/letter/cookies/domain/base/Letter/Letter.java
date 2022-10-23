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

    private String letterTitle;

    private String letterContent;

    private String letterCategory;

    private String letterImageUrl;

    private Double x;

    private Double y;

    private long enableCount;   // 각 편지를 읽을 수 있는 횟수 : 초기 편지 생성 시 4로 고정

    private Boolean checkExist;   // 작성된 편지의 읽을 수 있는 횟수가 전부 사용된 경우 -> 노출을 하지 않을 상태 체크

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
