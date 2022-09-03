package com.letter.cookies.service;

import com.letter.cookies.domain.base.Letter.Letter;
import com.letter.cookies.domain.base.Letter.LetterRepository;
import com.letter.cookies.domain.base.Member.Member;
import com.letter.cookies.domain.base.Member.MemberRepository;
import com.letter.cookies.domain.base.ReadLetter.ReadLetter;
import com.letter.cookies.domain.base.ReadLetter.ReadLetterRepository;
import com.letter.cookies.dto.letter.response.LetterDetailResponse;
import com.letter.cookies.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.letter.cookies.dto.response.CustomResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LetterService {
    private final LetterRepository letterRepository;
    private final MemberRepository memberRepository;
    private final ReadLetterRepository readLetterRepository;

    @Transactional
    public LetterDetailResponse getById(long letterId, String user_id) throws BaseException {
        Member member = memberRepository.findById(UUID.fromString(user_id))
                .orElseThrow(() -> new BaseException(REQUEST_DATA_DOES_NOT_EXISTS));
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new BaseException(REQUEST_DATA_NULL));

        if (!readLetterRepository.existsByMemberAndLetter(member, letter) && letter.getMember() != member ) {
            readLetterRepository.save(ReadLetter.builder().member(member).letter(letter).build());
            if(member.getCookie() < 1){
                throw new BaseException(REQUEST_NOT_ENOUGH_COOKIE);
            }
            letter.biteEaten();
        }

        return LetterDetailResponse.builder().letterContent(letter.getLetterContent())
                .letterNickname(letter.getWriterNickname()).x(letter.getXAxis())
                .y(letter.getYAxis()).enableCount(letter.getEnableCount()).build();
    }
}
