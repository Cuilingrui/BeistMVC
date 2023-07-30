package com.shike.beistmvc.context.event;

import com.shike.beans.BeansException;
import com.shike.beans.factory.BeanFactory;
import com.shike.beans.factory.BeanFactoryAware;
import com.shike.context.ApplicationEvent;
import com.shike.context.ApplicationListener;
import com.shike.utils.ClassUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

    public final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();
    private BeanFactory factory;

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }

    public void setFactory(BeanFactory factory) {
        this.factory = factory;
    }

    public Collection<ApplicationListener>  getApplicationListeners(ApplicationEvent event) {

        return applicationListeners.stream().
                filter(applicationListener -> supportsEvent(applicationListener, event)).
                collect(Collectors.toCollection(LinkedList::new));
    }

    protected boolean supportsEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event){
        Class<? extends ApplicationListener> listenerClass = applicationListener.getClass();
        //如果该类被Cglib代理，那么就要获取父类
        Class<?> targetClass = ClassUtils.isCglibProxyClass(listenerClass) ?  listenerClass.getSuperclass() : listenerClass;

        //获取该类实现的接口
        Type genericInterface = targetClass.getGenericInterfaces()[0];

        //
        Type actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[0];

        String className = actualTypeArgument.getTypeName();

        Class<?> eventClassName;

        try {
            eventClassName = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new BeansException("wrong event class name "+ className);
        }

        return eventClassName.isAssignableFrom(event.getClass());
    }

    @Override
    public void setBeanFactory(BeanFactory factory) throws BeansException {
        this.factory = factory;
    }
}
