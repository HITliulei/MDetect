package com.ll.framework.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.joda.time.DateTime;

/**
 * @author Lei
 * @version 0.1
 * @date 2021/6/2
 */


@Aspect
public class MCollectDataAop {

    @Pointcut("execution(* *..controller.*.*(..))")
    public void mCollectDataAop() { }


    @Before("mCollectDataAop()")
    public void mCollectDataBefore(JoinPoint joinPoint){
        DateTime endTime = DateTime.now();
    }


    @AfterReturning("mCollectDataAop()")
    public void mCollectDataAfterReturning(JoinPoint joinPoint){

    }


}
