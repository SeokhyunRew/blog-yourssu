package com.yourssu.blogyourssu.aop;/*
 * created by seokhyun on 2024-09-15.
 */

import static com.yourssu.blogyourssu.aop.LogLevel.ERROR;
import static com.yourssu.blogyourssu.aop.LogLevel.INFO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PerformanceAndSuccessLoggingAspect {

    public static final String FAIL = "\uD83D\uDC1B 실패 ";
    public static final String SUCCESS = "⭐ 성공 ";
    public static final String START = "\uD83C\uDFC3 시작 ";
    public static final String END = "\uD83D\uDD1A 종료 ";

    @Pointcut("within(com.yourssu.blogyourssu..*) && !within(com.yourssu.blogyourssu.aop..*) && !within(com.yourssu.blogyourssu.config..*)  && !within(com.yourssu.blogyourssu.filter..*)")
    public void applicationPackagePointcut() {
    }

    @Around("applicationPackagePointcut()")
    public Object logPerformanceAndSuccess(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;

        result = printAndSaveLog(joinPoint);

        return result;
    }

    @Before("applicationPackagePointcut()")
    public void logMethodStart(JoinPoint joinPoint) {
        log.info(START + ": {}", joinPoint.getSignature().toShortString());
    }

    @After("applicationPackagePointcut()")
    public void logMethodEnd(JoinPoint joinPoint) {
        log.info(END + ": {}", joinPoint.getSignature().toShortString());
    }

    private Object printAndSaveLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result;

        long startTime = System.currentTimeMillis();
        long executionTime;

        try {
            result = joinPoint.proceed();

            executionTime = getExecutionTime(startTime);

            logAndSaveExecutionDetails(joinPoint, true, null, executionTime);
        } catch (Throwable throwable) {
            executionTime = getExecutionTime(startTime);

            logAndSaveExecutionDetails(joinPoint, false, throwable, executionTime);

            throw throwable;
        }
        return result;
    }

    private long getExecutionTime(long startTime) {
        return System.currentTimeMillis() - startTime;
    }

    private void logAndSaveExecutionDetails(ProceedingJoinPoint joinPoint, boolean isSuccess,
                                            Throwable throwable, long executionTime) {

        String status = isSuccess ? SUCCESS : FAIL;
        LogLevel logLevel = isSuccess ? INFO : ERROR;
        String message = throwable == null ? null : throwable.getMessage();
        log.info("{} {} , 실행 시간 : {}ms, 에러 메시지: {}", status, joinPoint.getSignature(), executionTime, message);
//        saveLog(joinPoint, status, message, logLevel, executionTime);
    }
}
