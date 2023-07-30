package com.shike.beistmvc.beans.factory.config;

import com.shike.context.event.ApplicationEventMulticaster;

public interface SingletonBeanRegistry {

    Object getSingleton(String beanName);

    void registerSingleton(String beanName, Object singletonObject);
}
