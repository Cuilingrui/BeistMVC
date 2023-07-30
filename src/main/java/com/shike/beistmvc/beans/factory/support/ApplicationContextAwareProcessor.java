package com.shike.beistmvc.beans.factory.support;

import com.shike.beans.BeansException;
import com.shike.beans.factory.ApplicationContextAware;
import com.shike.beans.factory.Aware;
import com.shike.beans.factory.config.BeanPostProcessor;
import com.shike.context.ApplicationContext;

public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ApplicationContext context;

    public ApplicationContextAwareProcessor(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware){
            ((ApplicationContextAware) bean).setApplicationContex(context);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
