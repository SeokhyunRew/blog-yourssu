package com.yourssu.blogyourssu.common.exception.securityexception;/*
 * created by seokhyun on 2024-09-18.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yourssu.blogyourssu.dto.response.ErrorResponse;
import com.yourssu.blogyourssu.dto.util.ErrorUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    // ObjectMapper를 의존성 주입으로 가져옴
    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        // JavaTimeModule을 명확히 추가하여 직렬화 문제 방지
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // ErrorUtil을 사용하여 에러 응답 생성
        ErrorResponse errorResponse = ErrorUtil.buildErrorResponse(
                accessDeniedException,
                HttpStatus.FORBIDDEN,
                new ServletWebRequest(request)
        ).getBody();

        // 응답에 JSON으로 에러 메시지 작성
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
