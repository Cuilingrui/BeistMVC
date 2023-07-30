package com.shike.beistmvc.webmvc.annotation;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
//@Import(DelegatingWebMvcConfiguration.class)
public @interface EnableWebMvc {
}