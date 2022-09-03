package com.letter.cookies.domain.base.ReadLetter;

import com.letter.cookies.domain.base.Letter.Letter;
import com.letter.cookies.domain.base.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadLetterRepository extends JpaRepository<ReadLetter, Long> {
    boolean existsByMemberAndLetter(Member member, Letter letter);
}
