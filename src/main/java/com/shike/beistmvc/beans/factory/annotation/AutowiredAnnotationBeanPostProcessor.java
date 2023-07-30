package com.shike.beistmvc.beans.factory.annotation;

import cn.hutool.core.bean.BeanUtil;
import com.shike.beans.BeansException;
import com.shike.beans.PropertyValues;
import com.shike.beans.factory.BeanFactory;
import com.shike.beans.factory.BeanFactoryAware;
import com.shike.beans.factory.ConfigurableListableBeanFactory;
import com.shike.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.shike.utils.ClassUtils;


import java.lang.reflect.Field;

public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private  ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory factory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory)factory;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }


    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        //判断当前的类是否被AOP代理。
        clazz = ClassUtils.isCglibProxyClass(clazz) ? clazz.getSuperclass() : clazz;

        for (Field declaredField : clazz.getDeclaredFields()) {
            Autowired autowired = declaredField.getAnnotation(Autowired.class);

            Object dependentBean = null;
            if (autowired != null){
                Class<?> type = declaredField.getType();
                Qualifier qualifier = declaredField.getAnnotation(Qualifier.class);
                String dependentBeanName = null;
                if (qualifier != null){
                    dependentBeanName = qualifier.value();
                    dependentBean = beanFactory.getBean(dependentBeanName, type);
                }else {
                    dependentBean = beanFactory.getBean(type);
                }

                BeanUtil.setFieldValue(bean, declaredField.getName(), dependentBean);
            }
        }
        return pvs;
    }



    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return null;
    }
}
