package com.linglan.smart.ioc;


import com.linglan.smart.annotation.Inject;
import com.linglan.smart.utils.ArrayUtil;
import com.linglan.smart.utils.CollectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 依赖注入助手类
 */
public final class IocHelper {

    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)) {
            for (Map.Entry<Class<?>, Object> beanEntry : beanMap.entrySet()) {
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();

                //获取bean类所有成员变量
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)) {
                    for (Field beanField : beanFields) {
                        //判断beanField是否有注解
                        if (beanField.isAnnotationPresent(Inject.class)) {
                            //在beanmap中获取对应实例
                            Class<?> beanFieldClass = beanField.getType();
                            Object beanFieldInstance = beanMap.get(beanFieldClass);
                            if (beanFieldInstance != null) {
                                ReflectionUtil.setFiled(beanInstance, beanField, beanFieldInstance);
                            }
                        }
                    }
                }
            }
        }
    }
}
