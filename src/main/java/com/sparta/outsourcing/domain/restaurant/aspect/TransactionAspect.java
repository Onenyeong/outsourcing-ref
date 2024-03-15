package com.sparta.outsourcing.domain.restaurant.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class TransactionAspect {
  @AfterReturning("execution(* com.sparta.outsourcing.domain.restaurant.service.*.*(..))")
  public void logTransactionSuccess(JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    log.info("Transaction completed successfully for method: {}", methodName);
  }
  @AfterThrowing("execution(* com.sparta.outsourcing.domain.restaurant.service.*.*(..))")
  public void logTransactionFailure(JoinPoint joinPoint) {
    String methodName = joinPoint.getSignature().getName();
    log.error("Transaction failed for method: {}", methodName);
  }
}
