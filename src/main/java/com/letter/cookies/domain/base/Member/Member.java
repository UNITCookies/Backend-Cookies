package com.letter.cookies.domain.base.Member;

import com.letter.cookies.domain.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

    private Long cookie;

    private Long memberWriteCountPerDay;

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
}
