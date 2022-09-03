package com.letter.cookies.service;

import com.letter.cookies.domain.base.Letter.Letter;
import com.letter.cookies.domain.base.Letter.LetterRepository;
import com.letter.cookies.domain.base.Member.Member;
import com.letter.cookies.domain.base.Member.MemberRepository;
import com.letter.cookies.dto.letter.request.LetterWriteDto;
import com.letter.cookies.dto.letter.request.LetterMapRequest;
import com.letter.cookies.dto.letter.response.LetterWriteListResponse;
import com.letter.cookies.dto.letter.response.LetterWriteResponse;
import com.letter.cookies.dto.letter.response.LetterMapResponse;
import com.letter.cookies.dto.response.CustomResponse;
import com.letter.cookies.domain.base.ReadLetter.ReadLetter;
import com.letter.cookies.domain.base.ReadLetter.ReadLetterRepository;
import com.letter.cookies.dto.letter.response.LetterDetailResponse;
import com.letter.cookies.exception.BaseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.UUID;

import static com.letter.cookies.dto.response.CustomResponseStatus.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.letter.cookies.dto.response.CustomResponseStatus.EXCEED_WRITER_LIMIT;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LetterService {

    private final MemberRepository memberRepository;
    private final LetterRepository letterRepository;
    private final ReadLetterRepository readLetterRepository;

    public LetterWriteResponse writeLetter(UUID memberId, LetterWriteDto letterWriteDto) throws BaseException {

        Member member = memberRepository.findById(memberId).get();

        // TODO 사용자의 오늘 작성한 편지의 개수가 <= 1 인지 체크 -> >=2 인 경우 작성 불가
        int currLetterCnt = 0;
        LocalDate today = LocalDate.now();
        List<Letter> memberCurrLetterList = letterRepository.findByMemberAndCreatedAt(member, today);
        if (memberCurrLetterList.size() >= 2) {
            // 작성 불가
            throw new BaseException(EXCEED_WRITER_LIMIT);
        }


        // 칸초, 오레오, 미쯔, 초코파이, 하리보, 초코송이, 꼬북칩, 비요뜨, 짱구, 배배
        String[] randomWriterNickNameList = {"칸초", "오레오", "미쯔", "초코파이", "하리보", "초코송이", "꼬북칩", "비요뜨", "짱구", "배배"};
        Random random = new Random();
        int randIdx = random.nextInt(randomWriterNickNameList.length - 1);
        String randWriterNickName = randomWriterNickNameList[randIdx];
        letterWriteDto.setWriterNickname(randWriterNickName);

        Letter updateLetter = letterWriteDto.toEntity(member);
        letterRepository.save(updateLetter);

        LetterWriteResponse letterWriteResponse = LetterWriteResponse.builder()
                .letter(updateLetter)
                .build();

        return letterWriteResponse;
    }

    public List<LetterWriteListResponse> getWriteLetterList(UUID memberId) {

        List<LetterWriteListResponse> letterWriteListResponseList = new ArrayList<>();
        Member member = memberRepository.findById(memberId).get();

        List<Letter> memberLetterList = letterRepository.findByMember(member);
        for (Letter letter : memberLetterList) {
            LetterWriteListResponse letterWriteListResponse = LetterWriteListResponse.builder()
                    .letter(letter)
                    .build();

            letterWriteListResponseList.add(letterWriteListResponse);
        }

        return letterWriteListResponseList;
    }

    @Transactional
    public LetterDetailResponse getById(long letterId, String user_id) throws BaseException {
        Member member = memberRepository.findById(UUID.fromString(user_id))
                .orElseThrow(() -> new BaseException(REQUEST_DATA_DOES_NOT_EXISTS));
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new BaseException(REQUEST_DATA_NULL));

        if (!readLetterRepository.existsByMemberAndLetter(member, letter) &&
                letter.getMember().getMemberId() != member.getMemberId() ) {
            readLetterRepository.save(ReadLetter.builder().member(member).letter(letter).build());
            if(member.getCookie() < 1){
                throw new BaseException(REQUEST_NOT_ENOUGH_COOKIE);
            }
            letter.biteEaten();
        }

        return LetterDetailResponse.builder().letterContent(letter.getLetterContent())
                .letterNickname(letter.getWriterNickname()).x(letter.getX())
                .y(letter.getY()).enableCount(letter.getEnableCount()).build();

    }

    @Transactional
    public Map<String, List<LetterMapResponse>> getLetterWithinRadius(LetterMapRequest letterMapRequest) {
        Map<String, List<LetterMapResponse>> resultLetterList = new HashMap<>();

        List<LetterMapResponse> letterList = letterRepository.findByXBetweenAndYBetween(letterMapRequest.getStartX(), letterMapRequest.getEndX(), letterMapRequest.getStartY(), letterMapRequest.getEndY()).stream()
                .map(LetterMapResponse::new)
                .collect(Collectors.toList());
        resultLetterList.put("all", letterList);

        List<LetterMapResponse> letterListWithinRadius = letterRepository.findWithinRadius(letterMapRequest.getCurMemberX(), letterMapRequest.getCurMemberY()).stream()
                .map(LetterMapResponse::new)
                .collect(Collectors.toList());
        resultLetterList.put("radius", letterListWithinRadius);

        return resultLetterList;
    }

}
