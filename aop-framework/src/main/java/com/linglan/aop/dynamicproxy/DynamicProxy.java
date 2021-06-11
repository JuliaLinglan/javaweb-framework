package com.linglan.aop.dynamicproxy;

import com.linglan.aop.staticproxy.Hello;
import com.linglan.aop.staticproxy.HelloImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy implements InvocationHandler {
    private Object target;

    public DynamicProxy(Object target) {
        this.target = target;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object result = method.invoke(target, args);
        after();
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy() {
        return (T) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                this
        );
    }

    private void before() {
        System.out.println("Before...");
    }

    private void after() {
        System.out.println("After...");
    }


    public static void main(String[] args) {
        Hello hello = new HelloImpl();

        DynamicProxy proxy = new DynamicProxy(hello);

        Hello helloProxy = proxy.getProxy();
        helloProxy.say("Linglan");
    }
}
