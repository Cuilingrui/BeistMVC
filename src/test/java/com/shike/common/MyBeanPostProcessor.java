package com.shike.common;

import com.shike.bean.UserService;
import com.shike.beans.BeansException;
import com.shike.beans.factory.config.BeanPostProcessor;

public class MyBeanPostProcessor  implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

        return bean ;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if ("userService".equals(beanName)){
            UserService userService = (UserService)bean;
            userService.setLocation("修改");
        }
        return bean;
    }
}
