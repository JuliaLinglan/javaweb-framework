package com.linglan.aop.springaop;

import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class HelloBeforeAndAfterAdvice implements MethodBeforeAdvice, AfterReturningAdvice {
    public void afterReturning(Object o, Method method, Object[] objects, Object o1) throws Throwable {
        System.out.println("BeforeAndAfter after.");
    }

    public void before(Method method, Object[] objects, Object o) throws Throwable {
        System.out.println("BeforeAndAfter before.");
    }
}
