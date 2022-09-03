package com.letter.cookies.domain.base.Letter;

import com.letter.cookies.domain.base.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LetterRepository extends JpaRepository<Letter, Long> {

    List<Letter> findByMember(Member member);

    List<Letter> findByMemberAndCreatedAt(Member member, LocalDate createdAt);
}
