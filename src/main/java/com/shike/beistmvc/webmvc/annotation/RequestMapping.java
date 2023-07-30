package com.shike.beistmvc.webmvc.annotation;

import io.netty.handler.codec.http.HttpMethod;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    String path();

    String method() default "GET";

}
