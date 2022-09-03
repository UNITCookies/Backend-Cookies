package com.letter.cookies.domain.base.Member;

import com.letter.cookies.domain.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private UUID memberId;

    private String memberName;

    private String memberPassword;

    private Long cookie;

}
