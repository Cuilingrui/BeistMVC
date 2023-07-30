package com.shike.beistmvc.beans.factory.support;

import com.shike.beans.BeansException;
import com.shike.beans.factory.FactoryBean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

    protected static final Object NULL_OBJECT = new Object();

    //FactoryBean存放位置
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    protected Object getCachedObjectForFactoryBean(String beanName) {
        Object bean = this.factoryBeanObjectCache.get(beanName);
        return bean == NULL_OBJECT ? null : bean;

    }

    protected Object getObjectFromFactoryBean(FactoryBean factoryBean, String beanName) {
        //如果是单例的，就从缓存中获取，缓存中没有，调用FactoryBean中的getObject方法获取
        if (factoryBean.isSingleton()) {
            Object object = this.getCachedObjectForFactoryBean(beanName);

            if (object == null) {
                object = doGetObjectFromFactoryBean(factoryBean, beanName);
                this.factoryBeanObjectCache.put(beanName, (object == null) ? NULL_OBJECT : object);
            }

            return object == NULL_OBJECT ? null : object;
        }else {
            return doGetObjectFromFactoryBean(factoryBean, beanName);
        }
    }

    private Object doGetObjectFromFactoryBean(FactoryBean factoryBean, String beanName) {
        try {
            return factoryBean.getObject();
        }catch (Exception e){
            throw new BeansException("FactoryBean throw exception on object[" + beanName + "] creation", e);
        }
    }
}
