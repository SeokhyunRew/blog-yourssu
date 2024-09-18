package com.yourssu.blogyourssu.common.exception.customexception;/*
 * created by seokhyun on 2024-09-17.
 */

public class ExistException extends RuntimeException {

    public ExistException(String message) {
        super(message);
    }

    public ExistException(String message, Throwable cause) {
        super(message, cause);
    }

}
