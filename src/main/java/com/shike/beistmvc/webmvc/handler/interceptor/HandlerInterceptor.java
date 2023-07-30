package com.shike.beistmvc.webmvc.handler.interceptor;

import com.shike.webmvc.ModelAndView;
import com.shike.webmvc.http.BeistHttpRequest;
import com.shike.webmvc.http.BeistHttpResponse;


public interface HandlerInterceptor {

    default boolean preHandle(BeistHttpRequest request, BeistHttpResponse response, Object handler)
            throws Exception {
        return true;
    }

    default void postHandle(BeistHttpRequest request, BeistHttpResponse response, Object handler,
                             ModelAndView modelAndView) throws Exception {
    }

    default void afterCompletion(BeistHttpRequest request, BeistHttpResponse response, Object handler,
                                  Exception ex) throws Exception {
    }
}
