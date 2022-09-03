package com.letter.cookies.controller;

import com.letter.cookies.dto.letter.request.LetterMapStringRequest;
import com.letter.cookies.dto.letter.request.LetterWriteDto;
import com.letter.cookies.dto.letter.response.LetterReadListResponse;
import com.letter.cookies.dto.letter.request.LetterMapRequest;
import com.letter.cookies.dto.letter.response.LetterWriteListResponse;
import com.letter.cookies.dto.letter.response.LetterWriteResponse;
import com.letter.cookies.dto.letter.response.LetterDetailResponse;
import com.letter.cookies.dto.letter.response.LetterMapResponse;
import com.letter.cookies.dto.response.CustomResponse;
import com.letter.cookies.exception.BaseException;
import com.letter.cookies.service.LetterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.Map;

import static com.letter.cookies.dto.response.CustomResponseStatus.*;
import static com.letter.cookies.dto.response.CustomResponseStatus.SUCCESS;

@Slf4j
@RestController
@RequestMapping("/letter")
@RequiredArgsConstructor
public class LetterController {

    private final LetterService letterService;

    @PostMapping("/write")
    public ResponseEntity<CustomResponse> writeLetter(
            @RequestParam UUID memberId, @RequestBody LetterWriteDto letterWriteDto) {

        try {
            LetterWriteResponse letterWriteResponse = letterService.writeLetter(memberId, letterWriteDto);
            return new CustomResponse<>(letterWriteResponse, SAVE_LETTER).toResponseEntity();
        } catch (BaseException e) {
            return new CustomResponse<>(e.getStatus()).toResponseEntity();
        }
    }

    @GetMapping("/write/list")
    public ResponseEntity<CustomResponse> getWriteLetterList(@RequestParam UUID memberId) {

        List<LetterWriteListResponse> letterWriteListResponseList = letterService.getWriteLetterList(memberId);

        return new CustomResponse<>(letterWriteListResponseList, GET_WRITER_LETTER_LIST).toResponseEntity();
    }

    @GetMapping("/read/list")
    public ResponseEntity<CustomResponse> getReadLetterList(@RequestParam UUID memberId) throws BaseException {
        try{
            List<LetterReadListResponse> letterReadListResponseList = letterService.getByMemberReadLetter(memberId);
            return  new CustomResponse<>(letterReadListResponseList, GET_READ_LETTER_LIST).toResponseEntity();
        }catch (BaseException e){
            return new CustomResponse<>(e.getStatus()).toResponseEntity();
        }


    }

    @GetMapping("/{letterId}")
    public ResponseEntity<CustomResponse> getLetterDetail(@PathVariable long letterId,
                                                          @RequestParam UUID memberId)
            throws BaseException {
        try {
            LetterDetailResponse letterDetailResponse = letterService.getById(letterId, memberId);
            return new CustomResponse<>(letterDetailResponse, SUCCESS).toResponseEntity();
        } catch (BaseException e) {
            return new CustomResponse<>(e.getStatus()).toResponseEntity();
        }

    }

    @PostMapping("/map")
    public ResponseEntity<CustomResponse> getLetterWithinRadius(@RequestBody LetterMapStringRequest letterMapStringRequest) {
        log.debug("========================= Start Get Letter With Radius =========================");
        try {
            Map<String, List<LetterMapResponse>> letterList = letterService.getLetterWithinRadius(letterMapStringRequest);
            return new CustomResponse<>(letterList).toResponseEntity();
        } catch (Exception e) {
            return new CustomResponse<>(e.getMessage()).toResponseEntity();
        }
    }

}
