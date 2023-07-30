package com.shike.beistmvc.webmvc.handler.mapping;


import cn.hutool.core.annotation.AnnotationUtil;
import com.shike.beans.BeansException;
import com.shike.beans.factory.ApplicationContextAware;
import com.shike.beans.factory.InitializingBean;
import com.shike.context.ApplicationContext;
import com.shike.webmvc.annotation.Controller;
import com.shike.webmvc.annotation.RequestMapping;
import com.shike.webmvc.exception.NoHandlerFoundException;
import com.shike.webmvc.handler.HandlerExecutionChain;
import com.shike.webmvc.handler.HandlerMethod;
import com.shike.webmvc.handler.interceptor.HandlerInterceptor;
import com.shike.webmvc.handler.interceptor.MappedInterceptor;
import com.shike.webmvc.http.BeistHttpRequest;



import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class RequestMappingHandlerMapping implements ApplicationContextAware, HandlerMapping, InitializingBean {

    private MappingRegistry mappingRegistry = new MappingRegistry();
    private List<MappedInterceptor> interceptors = new ArrayList<>();

    private ApplicationContext context;

    public void setInterceptors(List<MappedInterceptor> interceptors) {
        this.interceptors = interceptors;
    }

    public MappingRegistry getMappingRegistry() {
        return mappingRegistry;
    }

    public void afterPropertiesSet() throws Exception {
        initialHandlerMethods();
    }

    private void initialHandlerMethods() {

        Map<String, Object> beansOfMap = context.getBeansOfType(Object.class);
        beansOfMap.entrySet().stream()
                .filter(entry -> this.isHandler(entry.getValue()))
                .forEach(entry -> this.detectHandlerMethods(entry.getKey(), entry.getValue()));
    }

    /**
     * 类上有标记Controller的注解就是我们需要找的handler
     *
     * @param handler
     * @return
     */
    private boolean isHandler(Object handler) {
        Class<?> beanType = handler.getClass();
        return (AnnotationUtil.hasAnnotation(beanType, Controller.class));
    }

    /**
     * 解析出handler中 所有被RequestMapping注解的方法
     *
     * @param beanName
     * @param handler
     */
    private void detectHandlerMethods(String beanName, Object handler) {
        Class<?> beanType = handler.getClass();
        Map<Method, RequestMappingInfo> methodsOfMap = MethodIntrospector.selectMethods(beanType,
                (MethodIntrospector.MetadataLookup<RequestMappingInfo>) method -> getMappingForMethod(method, beanType));

        methodsOfMap.forEach((method, requestMappingInfo) -> this.mappingRegistry.register(requestMappingInfo, handler, method));
    }

    /**
     * 查找method上面是否有RequestMapping，有 => 构建RequestMappingInfo
     *
     * @param method
     * @param beanType
     * @return
     */
    private RequestMappingInfo getMappingForMethod(Method method, Class<?> beanType) {
        RequestMapping requestMapping = AnnotationUtil.getAnnotation(method, RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            return null;
        }
        String prefix = getPathPrefix(beanType);
        return new RequestMappingInfo(prefix, requestMapping);
    }

    private String getPathPrefix(Class<?> beanType) {
        RequestMapping requestMapping = AnnotationUtil.getAnnotation(beanType, RequestMapping.class);
        if (Objects.isNull(requestMapping)) {
            return "";
        }
        return requestMapping.path();
    }

    @Override
    public HandlerExecutionChain getHandler(BeistHttpRequest request) throws Exception {
        String lookupPath = request.getRequestUrl();
        HandlerMethod handler = mappingRegistry.getHandlerMethodByPath(lookupPath);
        if (Objects.isNull(handler)) {
            throw new NoHandlerFoundException(request);
        }
        return createHandlerExecutionChain(lookupPath, handler);
    }

    private HandlerExecutionChain createHandlerExecutionChain(String lookupPath, HandlerMethod handler) {
        List<HandlerInterceptor> interceptors = this.interceptors.stream()
                .filter(mappedInterceptor -> mappedInterceptor.matches(lookupPath))
                .collect(toList());
        return new HandlerExecutionChain(handler, interceptors);
    }

    @Override
    public void setApplicationContex(ApplicationContext context) throws BeansException {
        this.context = context;
    }
}
