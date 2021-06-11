package com.linglan.aop.springaop;

import com.linglan.aop.staticproxy.Hello;
import com.linglan.aop.staticproxy.HelloImpl;
import org.springframework.aop.framework.ProxyFactory;

public class Client {

    public static void main(String[] args) {
        /**
         * 创建代理工厂
         */
        ProxyFactory proxyFactory = new ProxyFactory();
        /**
         * 射入目标类对象
         */
        proxyFactory.setTarget(new HelloImpl());
//        /**
//         * 添加前置增强
//         */
//        proxyFactory.addAdvice(new HelloBeforeAdvice());
//        /**
//         * 添加后置增强
//         */
//        proxyFactory.addAdvice(new HelloAfterAdvice());
        proxyFactory.addAdvice(new HelloAroundAdvice());
        /**
         * 从代理工厂获取代理
         */
        Hello hello = (Hello) proxyFactory.getProxy();
        /**
         * 调用代理的方法
         */
        hello.say("Ko");
    }

}
