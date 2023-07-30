package com.shike.beistmvc.beans.factory;

import com.shike.beans.BeansException;

public interface ObjectFactory<T> {
    T getObject() throws BeansException;
}
