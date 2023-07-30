package com.shike.beistmvc.beans.factory;

import com.shike.beans.BeansException;

public interface BeanFactoryAware extends Aware{

    void setBeanFactory(BeanFactory factory) throws BeansException;
}
