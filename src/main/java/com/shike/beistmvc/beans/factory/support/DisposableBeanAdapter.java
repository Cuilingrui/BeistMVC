package com.shike.beistmvc.beans.factory.support;

import cn.hutool.core.util.StrUtil;
import com.shike.beans.factory.DisposableBean;

import java.lang.reflect.Method;

public class DisposableBeanAdapter implements DisposableBean {
    private final Object bean;

    private final String beanName;

    private final String destroyName;

    public DisposableBeanAdapter(Object bean, String beanName, String destroyName) {
        this.bean = bean;
        this.beanName = beanName;
        this.destroyName = destroyName;
    }

    @Override
    public void destroy() throws Exception {
        if (bean instanceof  DisposableBean){
            ((DisposableBean) bean).destroy();
        }

        if (StrUtil.isNotEmpty(destroyName) && !(bean instanceof DisposableBean)){
            Method destroyMethod = bean.getClass().getMethod(destroyName);

            destroyMethod.invoke(bean);
        }
    }
}
