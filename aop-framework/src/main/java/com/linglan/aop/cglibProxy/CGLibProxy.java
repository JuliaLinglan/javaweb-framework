package com.linglan.aop.cglibProxy;

import com.linglan.aop.staticproxy.Hello;
import com.linglan.aop.staticproxy.HelloImpl;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

//CGLibProxy
public class CGLibProxy implements MethodInterceptor {

    public <T> T getProxy(Class<T> cls) {
        return (T) Enhancer.create(cls, this);
    }

    private static CGLibProxy instance = new CGLibProxy();

//    public CGLibProxy() {
//    }

    public static CGLibProxy getInstance() {
        return instance;
    }

    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        before();
        Object result = methodProxy.invokeSuper(o, objects);
        after();
        return result;
    }

    private void before() {
        System.out.println("Before...");
    }

    private void after() {
        System.out.println("After...");
    }

    public static void main(String[] args) {
//        CGLibProxy proxy = new CGLibProxy();
//        Hello helloProxy = proxy.getProxy(HelloImpl.class);

        Hello helloProxy = CGLibProxy.getInstance().getProxy(HelloImpl.class);
        helloProxy.say("Julia");

    }
}
