package com.linglan.smart.init;

import com.linglan.smart.classp.ClassHelper;
import com.linglan.smart.classp.ClassUtil;
import com.linglan.smart.ioc.BeanHelper;
import com.linglan.smart.ioc.IocHelper;
import com.linglan.smart.request.ControllerHelper;

public class HelperLoader {
    public static void init() {
        Class<?>[] classList = {
                ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> cls : classList) {
            ClassUtil.loadClass(cls.getName());
        }
    }
}
