package com.shike.beistmvc.aop.framework;

import com.shike.aop.AdvisedSupport;

public class ProxyFactory extends AdvisedSupport{

    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    private AopProxy createAopProxy() {
        if (this.isProxyTargetClass() || this.getTargetSource().getTargetClass().length == 0) {
            return new Cglib2AopProxy(this);
        }

        return new JdkDynamicAopProxy(this);
    }
}
