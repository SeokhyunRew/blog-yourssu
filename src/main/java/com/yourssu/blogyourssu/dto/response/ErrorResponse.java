package com.yourssu.blogyourssu.dto.response;/*
 * created by seokhyun on 2024-09-19.
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private LocalDateTime time;
    private HttpStatus status;
    private String message;
    private String requestURI;
}
