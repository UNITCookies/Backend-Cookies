package com.letter.cookies.service;

import com.letter.cookies.domain.base.Letter.Letter;
import com.letter.cookies.domain.base.Letter.LetterRepository;
import com.letter.cookies.domain.base.Member.Member;
import com.letter.cookies.domain.base.Member.MemberRepository;
import com.letter.cookies.dto.letter.request.LetterWriteDto;
import com.letter.cookies.dto.letter.response.LetterWriteListResponse;
import com.letter.cookies.dto.letter.response.LetterWriteResponse;
import com.letter.cookies.dto.letter.response.LetterReadListResponse;
import com.letter.cookies.domain.base.ReadLetter.ReadLetter;
import com.letter.cookies.domain.base.ReadLetter.ReadLetterRepository;
import com.letter.cookies.dto.letter.response.LetterDetailResponse;
import com.letter.cookies.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static com.letter.cookies.dto.response.CustomResponseStatus.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


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

        // TODO 사용자의 오늘 작성한 편지의 개수가 <= 1 인지 체크 -> >=2 인 경우 작성 불가
        LocalDateTime startDatetime = LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(0,0,0));   // 어제 00:00:00
        LocalDateTime endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59));   // 오늘 23:59:59
        List<Letter> memberCurrLetterList = letterRepository.findByMemberAndCreatedAtBetween(member, startDatetime, endDatetime);
        System.out.println(memberCurrLetterList.size());
        if (memberCurrLetterList.size() >= 2) {
            // 작성 불가
            throw new BaseException(EXCEED_WRITE_LIMIT);
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
                letter.getMember().getMemberId() != member.getMemberId()) {
            readLetterRepository.save(ReadLetter.builder().member(member).letter(letter).build());
            if (member.getCookie() < 1) {
                throw new BaseException(REQUEST_NOT_ENOUGH_COOKIE);
            }
            letter.biteEaten();
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
}
