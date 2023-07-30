package com.shike.common;

import com.shike.beans.BeansException;
import com.shike.beans.PropertyValue;
import com.shike.beans.PropertyValues;
import com.shike.beans.factory.ConfigurableListableBeanFactory;
import com.shike.beans.factory.config.BeanDefinition;
import com.shike.beans.factory.config.BeanFactoryPostProcessor;

public class MyBeanFactoryPostProcessor  implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition userService = beanFactory.getBeanDefinition("userService");

        PropertyValues propertyValues = userService.getPropertyValues();

        propertyValues.addPropertyValue(new PropertyValue("company", "修改"));
    }
}
