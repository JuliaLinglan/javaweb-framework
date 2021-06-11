package com.linglan.aop.springaop;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

public class HelloAfterAdvice implements AfterReturningAdvice {
    public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {
        System.out.println("After......");
    }
}
