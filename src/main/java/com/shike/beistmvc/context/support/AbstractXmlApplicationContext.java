package com.shike.beistmvc.context.support;

import com.shike.beans.factory.support.DefaultListableBeanFactory;
import com.shike.beans.factory.xml.XmlBeanDefinitionReader;

public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext{

    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) {
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory,this);

        String[] configLocations = getConfigLocations();
        if (configLocations != null){
            reader.loadBeanDefinitions(configLocations);
        }
    }

    protected abstract String[] getConfigLocations();
}
