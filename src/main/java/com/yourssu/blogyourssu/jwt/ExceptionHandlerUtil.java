package com.yourssu.blogyourssu.jwt;/*
 * created by seokhyun on 2024-09-19.
 */

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.HttpStatus;

public class ExceptionHandlerUtil {

    // 공통 예외 처리 메서드
    public static void handleException(HttpServletResponse response, String message, HttpStatus status, HttpServletRequest request) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        String jsonResponse = String.format(
                "{\"time\": \"%s\", \"status\": \"%s\", \"message\": \"%s\", \"requestURI\": \"%s\"}",
                LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                status.name(),
                message,
                request.getRequestURI()
        );

        response.getWriter().write(jsonResponse);
    }
}
