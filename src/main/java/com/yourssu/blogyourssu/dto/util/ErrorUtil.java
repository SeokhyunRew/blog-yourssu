package com.yourssu.blogyourssu.dto.util;/*
 * created by seokhyun on 2024-09-19.
 */

import com.yourssu.blogyourssu.dto.response.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;

public class ErrorUtil {

    public static ResponseEntity<ErrorResponse> buildErrorResponse(Exception e, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status,
                e.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(errorResponse, headers, status);
    }
}
