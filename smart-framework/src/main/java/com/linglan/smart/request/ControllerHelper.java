package com.linglan.smart.request;

import com.linglan.smart.annotation.Action;
import com.linglan.smart.classp.ClassHelper;
import com.linglan.smart.utils.ArrayUtil;
import com.linglan.smart.utils.CollectionUtil;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 控制器助手类
 */
public class ControllerHelper {
    /**
     * 请求与处理器映射
     */
    private static final Map<Request, Handler> ACTION_MAP = new HashMap<Request, Handler>();

    static {
        //获取所有controller类
        Set<Class<?>> controllerClassSet = ClassHelper.getControllerClassSet();
        if (CollectionUtil.isNotEmpty(controllerClassSet)) {
            for (Class<?> controllerClass : controllerClassSet) {
                //获取类中定义的方法
                Method[] methods = controllerClass.getDeclaredMethods();
                if (ArrayUtil.isNotEmpty(methods)) {
                    for (Method method : methods) {
                        //方法是否有action注解
                        if (method.isAnnotationPresent(Action.class)) {
                            Action action = method.getAnnotation(Action.class);
                            String mapping = action.value();

                            //验证url映射规范
                            if (mapping.matches("\\w+:/\\w*")) {
                                String[] array = mapping.split(":");
                                if (ArrayUtil.isNotEmpty(array) && array.length == 2) {
                                    //获取请求方法与路径
                                    String requestMethod = array[0];
                                    String requestPath = array[1];

                                    Request request = new Request(requestMethod, requestPath);
                                    Handler handler = new Handler(controllerClass, method);

                                    ACTION_MAP.put(request, handler);
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    public static Handler getHandler(String requestMethod, String requestPath) {
        Request request = new Request(requestMethod, requestPath);
        return ACTION_MAP.get(request);

    }
}
