package com.letter.cookies.exception;

import com.letter.cookies.dto.response.CustomResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BaseException extends Exception {

    private CustomResponseStatus status;

}