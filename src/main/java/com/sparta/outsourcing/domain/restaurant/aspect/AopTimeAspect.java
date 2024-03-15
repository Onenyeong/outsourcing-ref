package com.sparta.outsourcing.domain.restaurant.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class AopTimeAspect {
  @Around("execution( * com.sparta.outsourcing.domain.restaurant.service.*.*(..))")
  // 메서드 실행 전과 후 모두 advice 동작
  public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long startTime = System.currentTimeMillis();
    Object result = joinPoint.proceed(); // 메서드 실행
    long endTime = System.currentTimeMillis();
    long executionTime = endTime - startTime;
    log.info(joinPoint.getSignature().getName() + " executed in " + executionTime + "ms");
    return result;
  }
}
