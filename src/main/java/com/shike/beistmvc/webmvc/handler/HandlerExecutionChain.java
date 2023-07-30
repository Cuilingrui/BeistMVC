package com.shike.beistmvc.webmvc.handler;



import cn.hutool.core.collection.CollectionUtil;
import com.shike.webmvc.ModelAndView;
import com.shike.webmvc.handler.interceptor.HandlerInterceptor;
import com.shike.webmvc.http.BeistHttpRequest;
import com.shike.webmvc.http.BeistHttpResponse;


import java.util.ArrayList;
import java.util.List;

public class HandlerExecutionChain {
    private HandlerMethod handler;
    private List<HandlerInterceptor> interceptors = new ArrayList<>();
    private int interceptorIndex = -1;

    public HandlerExecutionChain(HandlerMethod handler, List<HandlerInterceptor> interceptors) {
        this.handler = handler;
        if (!CollectionUtil.isEmpty(interceptors)) {
            this.interceptors = interceptors;
        }
    }

    public boolean applyPreHandle(BeistHttpRequest request, BeistHttpResponse response) throws Exception {
        if (CollectionUtil.isEmpty(interceptors)) {
            return true;
        }
        for (int i = 0; i < interceptors.size(); i++) {
            HandlerInterceptor interceptor = interceptors.get(i);
            if (!interceptor.preHandle(request, response, this.handler)) {
                triggerAfterCompletion(request, response, null);
                return false;
            }
            this.interceptorIndex = i;
        }
        return true;
    }

    public void applyPostHandle(BeistHttpRequest request, BeistHttpResponse response, ModelAndView mv) throws Exception {
        if (CollectionUtil.isEmpty(interceptors)) {
            return;
        }
        for (int i = interceptors.size() - 1; i >= 0; i--) {
            HandlerInterceptor interceptor = interceptors.get(i);
            interceptor.postHandle(request, response, this.handler, mv);
        }
    }

    public void triggerAfterCompletion(BeistHttpRequest request, BeistHttpResponse response, Exception ex)
            throws Exception {
        if (CollectionUtil.isEmpty(interceptors)) {
            return;
        }
        for (int i = this.interceptorIndex; i >= 0; i--) {
            HandlerInterceptor interceptor = interceptors.get(i);
            interceptor.afterCompletion(request, response, this.handler, ex);
        }
    }

    public List<HandlerInterceptor> getInterceptors() {
        return interceptors;
    }

    public HandlerMethod getHandler() {
        return handler;
    }
}
