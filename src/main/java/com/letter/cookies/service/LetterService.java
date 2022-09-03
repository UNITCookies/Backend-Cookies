package com.letter.cookies.service;

import com.letter.cookies.domain.base.Letter.Letter;
import com.letter.cookies.domain.base.Letter.LetterRepository;
import com.letter.cookies.domain.base.Member.Member;
import com.letter.cookies.domain.base.Member.MemberRepository;
import com.letter.cookies.dto.letter.request.LetterMapStringRequest;
import com.letter.cookies.dto.letter.request.LetterWriteDto;
import com.letter.cookies.dto.letter.request.LetterMapRequest;
import com.letter.cookies.dto.letter.response.LetterWriteListResponse;
import com.letter.cookies.dto.letter.response.LetterWriteResponse;
import com.letter.cookies.dto.letter.response.LetterReadListResponse;
import com.letter.cookies.dto.letter.response.LetterMapResponse;
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

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static com.letter.cookies.dto.response.CustomResponseStatus.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LetterService {

    private final MemberRepository memberRepository;
    private final LetterRepository letterRepository;
    private final ReadLetterRepository readLetterRepository;

    @Transactional(readOnly = false)
    public LetterWriteResponse writeLetter(UUID memberId, LetterWriteDto letterWriteDto) throws BaseException {

        Member member = memberRepository.findById(memberId).get();

        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0,0,0));   // 어제 00:00:00
        LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));   // 오늘 23:59:59
        List<Letter> memberCurrLetterList = letterRepository.findByMemberAndCreatedAtBetween(member, startDatetime, endDatetime);
        System.out.println(memberCurrLetterList.size());
        if (memberCurrLetterList.size() >= 2) {
            throw new BaseException(EXCEED_WRITE_LIMIT);
        }

        String[] randomWriterNickNameList = {"칸초", "오레오", "미쯔", "초코파이", "하리보", "초코송이", "꼬북칩", "비요뜨", "짱구", "배배"};
        Random random = new Random();
        int randIdx = random.nextInt(randomWriterNickNameList.length - 1);
        String randWriterNickName = randomWriterNickNameList[randIdx];
        letterWriteDto.setWriterNickname(randWriterNickName);

        Letter updateLetter = letterWriteDto.toEntity(member);
        letterRepository.save(updateLetter);
        member.updateCookie();   // member 의 cookie 개수 1개 추가
        memberRepository.save(member);

        LetterWriteResponse letterWriteResponse = LetterWriteResponse.builder()
                .letter(updateLetter)
                .member(member)
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
    public LetterDetailResponse getById(long letterId, UUID memberId) throws BaseException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(REQUEST_DATA_DOES_NOT_EXISTS));
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new BaseException(REQUEST_DATA_NULL));

        if (!readLetterRepository.existsByMemberAndLetter(member, letter) &&
                letter.getMember().getMemberId() != member.getMemberId()) {
            readLetterRepository.save(ReadLetter.builder().member(member).letter(letter).build());
            if (member.getCookie() < 1) {
                throw new BaseException(REQUEST_NOT_ENOUGH_COOKIE);
            }

            letter.biteEaten();
            member.cookieSpent();

            letterRepository.save(letter);
            memberRepository.save(member);
        }

        return LetterDetailResponse.builder().letterContent(letter.getLetterContent())
                .letterNickname(letter.getWriterNickname()).x(letter.getX())
                .y(letter.getY()).enableCount(letter.getEnableCount()).build();

    }

    public List<LetterReadListResponse> getByMemberReadLetter(UUID userId) throws BaseException {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new BaseException(REQUEST_USER_NOT_EXISTS));
        List<ReadLetter> readLetters = readLetterRepository.findAllByMember(member);
        return LetterReadListResponse.listOf(readLetters.stream().map(readLetter ->
                readLetter.getLetter()).collect(Collectors.toList()));
    }

    @Transactional
    public Map<String, List<LetterMapResponse>> getLetterWithinRadius(LetterMapStringRequest letterMapStringRequest) {
        Map<String, List<LetterMapResponse>> resultLetterList = new HashMap<>();
        log.info("변경 전 타입: {}", letterMapStringRequest.getStartX().getClass().getName());
        LetterMapRequest letterMapRequest = letterMapStringRequest.toMapRequest();
        log.info("변경 후 타입: {}", letterMapRequest.getStartX().getClass().getName());

        log.info("지도 내의 모든 편지 조회");
        List<LetterMapResponse> letterList = letterRepository.findByXBetweenAndYBetween(letterMapRequest.getStartX(), letterMapRequest.getEndX(), letterMapRequest.getStartY(), letterMapRequest.getEndY()).stream()
                .map(LetterMapResponse::new)
                .collect(Collectors.toList());
        resultLetterList.put("all", letterList);

        log.info("반경 내의 편지 조회");
        List<LetterMapResponse> letterListWithinRadius = letterRepository.findWithinRadius(letterMapRequest.getCurMemberX(), letterMapRequest.getCurMemberY()).stream()
                .map(LetterMapResponse::new)
                .collect(Collectors.toList());
        resultLetterList.put("radius", letterListWithinRadius);

        return resultLetterList;
    }

}
