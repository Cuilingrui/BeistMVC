package com.shike.beistmvc.context.support;

import com.shike.beans.BeansException;
import com.shike.beans.factory.ConfigurableListableBeanFactory;
import com.shike.beans.factory.config.BeanFactoryPostProcessor;
import com.shike.beans.factory.config.BeanPostProcessor;
import com.shike.beans.factory.support.ApplicationContextAwareProcessor;
import com.shike.context.ApplicationEvent;
import com.shike.context.ApplicationListener;
import com.shike.context.ConfigurableApplicationContext;
import com.shike.context.event.ApplicationEventMulticaster;
import com.shike.context.event.ContextClosedEvent;
import com.shike.context.event.ContextRefreshedEvent;
import com.shike.context.event.SimpleApplicationEventMulticaster;
import com.shike.core.io.DefaultResourceLoader;

import java.util.Map;

public abstract class AbstractApplicationContext extends DefaultResourceLoader implements ConfigurableApplicationContext {
    private static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";
    private ApplicationEventMulticaster applicationEventMulticaster;
    @Override
    public void refresh() throws BeansException {

        //1.创建BeanFactory，并加载BeanDefinition
        refreshBeanFactory();

        //2. 获取BeanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();

        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        //3. 在Bean实例化之前，执行BeanFactoryPostProcessor
        invokeBeanFactoryPostProcessors(beanFactory);

        //4. 在Bean对象实例化之前提前注册BeanPostProcessor
        registerBeanPostProcessors(beanFactory);

        //5. 提前实例化单例Bean对象
        beanFactory.preInstantiateSingletons();

        //6. 初始化事件发布者
        initApplicationEventMulticaster();

        //7. 注册监听器
        registerListeners();

        //8. 完成刷新事件
        finishRefresh();
    }

    private void initApplicationEventMulticaster() {

        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    private void registerListeners(){
        Map<String, ApplicationListener> listener = getBeansOfType(ApplicationListener.class);

        listener.values().forEach(applicationEventMulticaster::addApplicationListener);
    }

    public void finishRefresh(){
        publishEvent(new ContextRefreshedEvent(this));
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        beanPostProcessorMap.values().forEach(beanFactory::addBeanPostProcessor);
    }

    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);

        beanFactoryPostProcessorMap.values().
                forEach(beanFactoryPostProcessor -> beanFactoryPostProcessor.postProcessBeanFactory(beanFactory));
    }

    protected abstract ConfigurableListableBeanFactory getBeanFactory();

    protected abstract void refreshBeanFactory() throws BeansException;


    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return getBeanFactory().getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return getBeanFactory().getBean(name, args);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public  <T> T getBean(Class<T> requiredType) throws BeansException{
     return getBeanFactory().getBean(requiredType);
    }

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        publishEvent(new ContextClosedEvent(this));

        getBeanFactory().destroySingletons();
    }
}
