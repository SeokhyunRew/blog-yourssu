package com.yourssu.blogyourssu.common.exception;/*
 * created by seokhyun on 2024-09-17.
 */

import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.yourssu.blogyourssu.common.exception.customexception.ExistException;
import com.yourssu.blogyourssu.common.exception.customexception.ExpiredException;
import com.yourssu.blogyourssu.common.exception.customexception.ForbiddenException;
import com.yourssu.blogyourssu.common.exception.customexception.NotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;

@RestControllerAdvice
@Getter
@Setter
@Slf4j
public class GlobalExceptionHandler {

    // 에러 응답 구조 정의
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorResponse {
        private LocalDateTime time;
        private HttpStatus status;
        private String message;
        private String requestURI;
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception e, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status,
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // 명시적으로 JSON 타입 설정

        return new ResponseEntity<>(errorResponse, status);
    }


//    @ExceptionHandler(RestClientResponseException.class)
//    public ResponseEntity<ErrorResponse> handleBadRequestException(RestClientResponseException e, WebRequest request) {
//        log.error(e.getMessage(), e);
//        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e, WebRequest request) {
//        log.error(e.getMessage(), e);
//        return buildErrorResponse(e, HttpStatus.UNAUTHORIZED, request);
//    }
//
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e, WebRequest request) {
        log.error(e.getMessage(), e);
        return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
    }

//    @ExceptionHandler(UsernameNotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e, WebRequest request) {
//        log.error(e.getMessage(), e);
//        return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
//    }
//
//    @ExceptionHandler(InternalAuthenticationServiceException.class)
//    public ResponseEntity<ErrorResponse> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e, WebRequest request) {
//        log.error(e.getMessage(), e);
//        return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
//    }
//
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(ExpiredJwtException e, WebRequest request) {
        log.error(e.getMessage(), e);
        return buildErrorResponse(e, HttpStatus.UNAUTHORIZED, request);
    }
//
//    @ExceptionHandler(ExistException.class)
//    public ResponseEntity<ErrorResponse> handleExistException(ExistException e, WebRequest request) {
//        log.error(e.getMessage(), e);
//        return buildErrorResponse(e, HttpStatus.CONFLICT, request);
//    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException e, WebRequest request) {
        log.error(e.getMessage(), e);
        return buildErrorResponse(e, HttpStatus.FORBIDDEN, request);
    }

//    @ExceptionHandler(ExpiredException.class)
//    public ResponseEntity<ErrorResponse> handleExpiredException(ExpiredException e, WebRequest request) {
//        log.error(e.getMessage(), e);
//        return buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
//    }

    //database unique인데 중복값시 예외 처리
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e, WebRequest request) {
        log.error(e.getMessage(), e);
        // 사용자 친화적인 메시지로 수정
        // 사용자 친화적인 메시지로 수정
        String customMessage = "이미 사용 중인 Email입니다. 새로운 Email을 등록해주세요.";

        // 사용자 친화적인 메시지로 새로운 Exception 객체 생성
        Exception customException = new Exception(customMessage);

        return buildErrorResponse(customException, HttpStatus.CONFLICT, request);
    }

    //클라이언트에서 문법이 잘못된 request 전송 예외처리
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, WebRequest request) {
        log.error(e.getMessage(), e);
        String customMessage = "요청한 request의 문법, 구조가 잘못됐습니다. 확인해주세요";

        // 사용자 친화적인 메시지로 새로운 Exception 객체 생성
        Exception customException = new Exception(customMessage);
        return buildErrorResponse(customException, HttpStatus.BAD_REQUEST, request);
    }
//
//    // ConstraintViolationException 예외 처리, Validation에 의해 걸린 잘못된 요청 예외 처리
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e, WebRequest request) {
//        log.error("ConstraintViolationException caught: ", e);
//        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
//    }

    //서버 내부 오류
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleHttpInternalServerException(Exception e, WebRequest request) {
        log.error(e.getMessage(), e);
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
//
//    // ConstraintViolationException 예외 처리, Validation에 의해 걸린 잘못된 요청 예외 처리
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, WebRequest request) {
//        log.error("ConstraintViolationException caught: ", e);
//        return buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
//    }

}
