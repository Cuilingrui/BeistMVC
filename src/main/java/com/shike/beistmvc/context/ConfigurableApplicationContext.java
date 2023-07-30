package com.shike.beistmvc.context;

import com.shike.beans.BeansException;

public interface ConfigurableApplicationContext extends ApplicationContext{
    void refresh() throws BeansException;

    void registerShutdownHook();

    void close();
}
