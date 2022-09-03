package com.letter.cookies.domain.base.Letter;

import com.letter.cookies.domain.base.Member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LetterRepository extends JpaRepository<Letter, Long> {

    List<Letter> findByMember(Member member);

    List<Letter> findByMemberAndCreatedAt(Member member, LocalDate createdAt);

    List<Letter> findByXBetweenAndYBetween(Double startX, Double endX, Double startY, Double endY);

    @Query(value = "select *, (6371*acos(cos(radians(x))*cos(radians(?1))*cos(radians(?2)-radians(y)) \n" +
            "+sin(radians(x))*sin(radians(?1)))) AS distance\n" +
            "from letter\n" +
            "having distance <= 0.25\n" +
            "order by distance DESC;",
            nativeQuery = true)
    List<Letter> findWithinRadius(Double curMemberX, Double curMemberY);

}
