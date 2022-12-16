package com.letter.cookies.service;

import com.letter.cookies.domain.base.Letter.Letter;
import com.letter.cookies.domain.base.Letter.LetterRepository;
import com.letter.cookies.domain.base.Member.Member;
import com.letter.cookies.domain.base.Member.MemberRepository;
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

import com.letter.cookies.external.ExternalRestful;
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
    private final ExternalRestful externalRestful;

    static final int LETTER_MIN_LENGTH = 1;
    static final int LETTER_MAX_LENGTH = 500;

    @Transactional(readOnly = false)
    public LetterWriteResponse writeLetter(UUID memberId, LetterWriteDto letterWriteDto) throws BaseException {

        // 편지 글자수 제한 체크
        if (letterWriteDto.getLetterContent().length() < LETTER_MIN_LENGTH) {
            throw new BaseException(UNDER_LETTER_MIN_LENGTH);
        }
        if (letterWriteDto.getLetterContent().length() > LETTER_MAX_LENGTH) {
            throw new BaseException(EXCEED_LETTER_MAX_LENGTH);
        }

        List<String> targetAddressList = new ArrayList<>();
        if (letterWriteDto.getLetterTitle() == null) {
            String addressName = externalRestful.getRegionAddress(externalRestful.conversion(
                    letterWriteDto.getX(), letterWriteDto.getY()
            ));
            letterWriteDto.setLetterTitle(addressName);
        }

        Member member = memberRepository.findById(memberId).get();

        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0,0,0));   // 어제 00:00:00
        LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));   // 오늘 23:59:59
        List<Letter> memberCurrLetterList = letterRepository.findByMemberAndCreatedAtBetween(member, startDatetime, endDatetime);
        System.out.println(memberCurrLetterList.size());
//        if (memberCurrLetterList.size() >= 2) {
//            throw new BaseException(EXCEED_WRITE_LIMIT);
//        }

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

    @Transactional
    public LetterDetailResponse getReadLetterById(long letterId, UUID memberId) throws BaseException {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(REQUEST_DATA_DOES_NOT_EXISTS));
        Letter letter = letterRepository.findById(letterId)
                .orElseThrow(() -> new BaseException(REQUEST_DATA_NULL));

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
    public Map<String, List<LetterMapResponse>> getLetterWithinRadius(LetterMapRequest letterMapRequest) {
        Map<String, List<LetterMapResponse>> resultLetterList = new HashMap<>();

        LetterMapRequest request = letterMapRequest.setStartXY();

        log.info("[LetterService] Find All Letter");
        List<LetterMapResponse> letterList = letterRepository.findWithinMap(request.getStartX(), request.getEndX(), request.getStartY(), request.getEndY()).stream()
                .map(LetterMapResponse::new)
                .collect(Collectors.toList());
        resultLetterList.put("all", letterList);

        log.info("[LetterService] Find Letter within radius");
        List<LetterMapResponse> letterListWithinRadius = letterRepository.findWithinRadius(request.getCurMemberX(), request.getCurMemberY(), request.getStartX(), request.getEndX(), request.getStartY(), request.getEndY()).stream()
                .map(LetterMapResponse::new)
                .collect(Collectors.toList());
        resultLetterList.put("radius", letterListWithinRadius);

        return resultLetterList;
    }

}
