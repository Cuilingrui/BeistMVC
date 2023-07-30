package com.shike.beistmvc.beans.factory;

public interface InitializingBean {

    void afterPropertiesSet() throws Exception;
}
