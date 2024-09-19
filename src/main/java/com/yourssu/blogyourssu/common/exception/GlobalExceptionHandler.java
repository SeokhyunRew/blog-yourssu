package com.yourssu.blogyourssu.common.exception;/*
 * created by seokhyun on 2024-09-17.
 */

import com.yourssu.blogyourssu.common.exception.customexception.ForbiddenException;
import com.yourssu.blogyourssu.common.exception.customexception.NotFoundException;
import com.yourssu.blogyourssu.dto.response.ErrorResponse;
import com.yourssu.blogyourssu.dto.util.ErrorUtil;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Getter
@Setter
@Slf4j
public class GlobalExceptionHandler {

    //요청 request에 해당하는 게시글, 댓글 등 찾지 못함
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e, WebRequest request) {
        log.error(e.getMessage(), e);
        return ErrorUtil.buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
    }

    //토큰 만료 예외
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(ExpiredJwtException e, WebRequest request) {
        log.error(e.getMessage(), e);
        return ErrorUtil.buildErrorResponse(e, HttpStatus.UNAUTHORIZED, request);
    }

    //jwt인증은 했지만 작성자 본인 권한 x
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException e, WebRequest request) {
        log.error(e.getMessage(), e);
        return ErrorUtil.buildErrorResponse(e, HttpStatus.FORBIDDEN, request);
    }

    //database unique인데 중복값시 예외 처리
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e, WebRequest request) {
        log.error(e.getMessage(), e);
        String customMessage = "이미 사용 중인 Email입니다. 새로운 Email을 등록해주세요.";
        Exception customException = new Exception(customMessage);
        return ErrorUtil.buildErrorResponse(customException, HttpStatus.CONFLICT, request);
    }

    //클라이언트에서 문법이 잘못된 request 전송 예외처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, WebRequest request) {
        log.error(e.getMessage(), e);
        String customMessage = "요청한 request의 문법, 구조가 잘못됐습니다. 확인해주세요";
        Exception customException = new Exception(customMessage);
        return ErrorUtil.buildErrorResponse(customException, HttpStatus.BAD_REQUEST, request);
    }

    //서버 내부 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleHttpInternalServerException(Exception e, WebRequest request) {
        log.error(e.getMessage(), e);
        return ErrorUtil.buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

}
