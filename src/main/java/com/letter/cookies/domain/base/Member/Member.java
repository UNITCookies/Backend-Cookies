package com.letter.cookies.domain.base.Member;

import com.letter.cookies.domain.base.BaseEntity;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Type(type = "uuid-char")
    private UUID memberId;

    private String memberName;

    private String memberPassword;

    private String memberEmail;

    private String identifier;

    private long cookie;   // 편지를 읽을 수 있는 (우유) 개수

    private long memberWriteCountPerDay;   // 하루 동안 작성한 편지 개수 -> 하루마다 0으로 업데이트

    @Builder
    public Member(String memberName, String memberPassword, String memberEmail, String identifier, Long cookie, Long memberWriteCountPerDay){
        this.memberName = memberName;
        this.memberPassword = memberPassword;
        this.memberEmail = memberEmail;
        this.identifier = identifier;
        this.cookie = (cookie == null) ? 0 : cookie;
        this.memberWriteCountPerDay = memberWriteCountPerDay;
    }

    public void update(String memberName) {
        this.memberName = memberName;
    }

    public void cookieSpent() {
        this.cookie--;
    }

    public void updateCookie() {
        this.cookie += 1;
    }

    public void updateWriteCountPerDay(long memberWriteCountPerDay) {
        this.memberWriteCountPerDay = memberWriteCountPerDay;
    }
}
