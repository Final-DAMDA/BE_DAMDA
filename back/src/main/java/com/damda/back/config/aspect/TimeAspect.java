package com.damda.back.config.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class TimeAspect {
    @Pointcut("@annotation(com.damda.back.config.annotation.TimeChecking)")
    private void enableTimer() {}


    @Around("enableTimer()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        var stopWatch = new StopWatch();
        stopWatch.start();

        Object result = joinPoint.proceed();

        stopWatch.stop();

        System.out.println("성능시간 : " + stopWatch.getTotalTimeMillis());

        return result;
    }
}
