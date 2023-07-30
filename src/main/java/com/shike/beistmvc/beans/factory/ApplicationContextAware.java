package com.shike.beistmvc.beans.factory;

import com.shike.beans.BeansException;
import com.shike.context.ApplicationContext;

public interface ApplicationContextAware extends Aware{
    void setApplicationContex(ApplicationContext context) throws BeansException;
}
