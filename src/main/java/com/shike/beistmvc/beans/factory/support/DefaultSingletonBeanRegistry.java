package com.shike.beistmvc.beans.factory.support;

import com.shike.beans.BeansException;
import com.shike.beans.factory.DisposableBean;
import com.shike.beans.factory.ObjectFactory;
import com.shike.beans.factory.config.SingletonBeanRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 该类主要负责存放单例实例，不负责创建
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    //一级缓存
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    //二级缓存，没有完全实例化的对象
    protected final Map<String, Object> earlySingletonObjects = new HashMap<>();

    //三级缓存,存放代理对象
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>();

    //随之定义的销毁方法
    Map<String, DisposableBean> disposableBeans = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        //先从一级缓存中取
        Object singletonObject = singletonObjects.get(beanName);

        if (singletonObject == null) {
            //再从二级缓存中取
            singletonObject = earlySingletonObjects.get(beanName);

            if (singletonObject == null) {
                //再从第三级缓存中取，然后调用getObject方法
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);

                if (singletonFactory != null) {
                    singletonObject = singletonFactory.getObject();
                    earlySingletonObjects.put(beanName, singletonObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }


    public void destroySingletons() {
        Object[] disposableName = disposableBeans.keySet().toArray();

        for (int i = disposableName.length - 1; i >= 0; i--) {
            Object beanName = disposableName[i];

            DisposableBean disposableBean = disposableBeans.remove(beanName);

            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
            }
        }
    }


    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
        earlySingletonObjects.remove(beanName);
        singletonFactories.remove(beanName);
    }

    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        if (!this.singletonObjects.containsKey(beanName)) {
            this.singletonFactories.put(beanName, singletonFactory);
            this.earlySingletonObjects.remove(beanName);
        }
    }

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }

}
