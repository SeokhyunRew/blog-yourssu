package com.yourssu.blogyourssu.common.exception.securityexception;/*
 * created by seokhyun on 2024-09-18.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yourssu.blogyourssu.common.exception.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    // ObjectMapper를 의존성 주입으로 가져옴
    public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        // 여기서 JavaTimeModule을 명확히 추가하여 직렬화 문제 방지
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name()); // UTF-8로 설정
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        // 에러 응답 구조 정의
        GlobalExceptionHandler.ErrorResponse errorResponse = new GlobalExceptionHandler.ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED,
                "요청된 토큰이 올바르지 않습니다",
                request.getRequestURI()
        );

        // 응답에 JSON으로 에러 메시지 작성
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
