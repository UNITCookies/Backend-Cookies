package com.letter.cookies.domain.base.ReadLetter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadLetterRepository extends JpaRepository<ReadLetter, Long> {
}
