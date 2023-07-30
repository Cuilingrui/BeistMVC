package com.shike.beistmvc.aop;

public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();

}
