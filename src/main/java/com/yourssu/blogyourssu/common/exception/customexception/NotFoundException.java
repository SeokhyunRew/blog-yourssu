package com.yourssu.blogyourssu.common.exception.customexception;/*
 * created by seokhyun on 2024-09-18.
 */

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
