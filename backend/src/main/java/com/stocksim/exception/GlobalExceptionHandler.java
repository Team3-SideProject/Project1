package com.stocksim.exception;


import com.stocksim.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 전역 예외처리
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalAccessError.class) // IllegalAccessError 발생시 아래 실행
    public ErrorResponse handleIllegalAccessError(IllegalAccessError e){
        return new ErrorResponse(e.getMessage()); // 에러 메세지 프론트 전송(throw 받은 메세지로)
    }
}
