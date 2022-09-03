package com.letter.cookies.controller;

import com.letter.cookies.dto.letter.response.LetterDetailResponse;
import com.letter.cookies.dto.response.CustomResponse;
import com.letter.cookies.exception.BaseException;
import com.letter.cookies.service.LetterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.letter.cookies.dto.response.CustomResponseStatus.SUCCESS;

@RestController
@RequestMapping("/letter")
@RequiredArgsConstructor
public class LetterController {
    private final LetterService letterService;

    @GetMapping("/{letterId}")
    public ResponseEntity<CustomResponse> getLetterDetail(@PathVariable long letterId,
                                                          @RequestParam String userId)
            throws BaseException {
        try {
            LetterDetailResponse letterDetailResponse = letterService.getById(letterId, userId);
            return new CustomResponse<>(letterDetailResponse, SUCCESS).toResponseEntity();
        } catch (BaseException e) {
            return new CustomResponse<>(e.getStatus()).toResponseEntity();
        }

    }
}