package com.shike.beistmvc.webmvc.handler.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.shike.webmvc.ModelAndView;
import com.shike.webmvc.http.BeistHttpRequest;
import com.shike.webmvc.http.BeistHttpResponse;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MappedInterceptor implements HandlerInterceptor {
    private List<String> includePatterns = new ArrayList<>();
    private List<String> excludePatterns = new ArrayList<>();

    private HandlerInterceptor interceptor;

    public MappedInterceptor(HandlerInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public HandlerInterceptor getInterceptor() {
        return interceptor;
    }

    /**
     * 添加支持的path
     *
     * @param patterns
     * @return
     */
    public MappedInterceptor addIncludePatterns(String... patterns) {
        this.includePatterns.addAll(Arrays.asList(patterns));
        return this;
    }

    /**
     * 添加排除的path
     *
     * @param patterns
     * @return
     */
    public MappedInterceptor addExcludePatterns(String... patterns) {
        this.excludePatterns.addAll(Arrays.asList(patterns));
        return this;
    }


    /**
     * 根据传入的path, 判断当前的interceptor是否支持
     *
     * @param lookupPath
     * @return
     */
    public boolean matches(String lookupPath) {
        if (!CollectionUtil.isEmpty(this.excludePatterns)) {
            if (excludePatterns.contains(lookupPath)) {
                return false;
            }
        }
        if (ObjectUtil.isEmpty(this.includePatterns)) {
            return true;
        }
        return includePatterns.contains(lookupPath);
    }


    @Override
    public boolean preHandle(BeistHttpRequest request, BeistHttpResponse response, Object handler)
            throws Exception {
        return this.interceptor.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(BeistHttpRequest request, BeistHttpResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        this.interceptor.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(BeistHttpRequest request, BeistHttpResponse response, Object handler,
                                 Exception ex) throws Exception {
        this.interceptor.afterCompletion(request, response, handler, ex);
    }
}
