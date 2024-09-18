package com.yourssu.blogyourssu.common.exception.customexception;/*
 * created by seokhyun on 2024-09-17.
 */

public class ExpiredException extends RuntimeException{

    public ExpiredException(String message) {
        super(message);
    }

    public ExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
