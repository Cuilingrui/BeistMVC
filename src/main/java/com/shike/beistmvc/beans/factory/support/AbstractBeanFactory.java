package com.shike.beistmvc.beans.factory.support;

import com.shike.beans.BeansException;
import com.shike.beans.factory.FactoryBean;
import com.shike.beans.factory.config.BeanDefinition;
import com.shike.beans.factory.config.BeanPostProcessor;
import com.shike.beans.factory.config.ConfigurableBeanFactory;
import com.shike.utils.ClassUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return (T) getBean(name);
    }

    protected <T> T doGetBean(String name, Object[] args) {
        Object bean = getSingleton(name);
        //判断该Bean是不是FactoryBean对象
        //如果是就从FactoryBean对象缓存中获取实例，如果没有缓存的话就调用FactoryBean::getObject方法
        if (bean != null) {
            return (T) getObjectForBeanInstance(bean, name);
        }
        //创建Bean和获取BeanDefinition的方式，交给子类来实现
        BeanDefinition beanDefinition = getBeanDefinition(name);

        bean = createBean(name, beanDefinition, args);

        return (T) getObjectForBeanInstance(bean, name);
    }

    private Object getObjectForBeanInstance(Object bean, String beanName) {
        if (!(bean instanceof FactoryBean)) {
            return bean;
        }

        Object object = getCachedObjectForFactoryBean(beanName);
        if (object == null){

            object = getObjectFromFactoryBean((FactoryBean<?>) bean, beanName);
        }
        return object;
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException;

    protected abstract BeanDefinition getBeanDefinition(String name) throws BeansException;

    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader;
    }
}
