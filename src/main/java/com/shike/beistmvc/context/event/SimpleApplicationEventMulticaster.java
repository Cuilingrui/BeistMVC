package com.shike.beistmvc.context.event;

import com.shike.beans.BeansException;
import com.shike.beans.factory.BeanFactory;
import com.shike.context.ApplicationEvent;
import com.shike.context.ApplicationListener;

import java.util.Collection;

public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {


    public SimpleApplicationEventMulticaster(BeanFactory beanFactory) {
        setBeanFactory(beanFactory);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void multicastEvent(final ApplicationEvent event) {
        getApplicationListeners(event).
                forEach(applicationListener -> applicationListener.onApplicationEvent(event));
    }
}
