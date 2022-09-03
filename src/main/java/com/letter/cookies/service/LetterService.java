package com.letter.cookies.service;

import com.letter.cookies.domain.base.Letter.Letter;
import com.letter.cookies.domain.base.Letter.LetterRepository;
import com.letter.cookies.domain.base.Member.MemberRepository;
import com.letter.cookies.dto.letter.response.LetterDetailResponse;
import com.letter.cookies.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.letter.cookies.dto.response.CustomResponseStatus.REQUEST_DATA_NULL;
import static com.letter.cookies.dto.response.CustomResponseStatus.REQUEST_USER_NOT_EXISTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LetterService {
    private final LetterRepository letterRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public LetterDetailResponse getById(long letterId, String user_id) throws BaseException {
        memberRepository.findById(UUID.fromString(user_id))
                .orElseThrow(() -> new BaseException(REQUEST_USER_NOT_EXISTS));
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new BaseException(REQUEST_DATA_NULL));
        letter.biteEaten();
        return LetterDetailResponse.builder().letterContent(letter.getLetterContent())
                .letterNickname(letter.getWriterNickname()).build();
    }
}
