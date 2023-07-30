package com.shike.beistmvc.webmvc.handler.mapping;


import com.shike.webmvc.annotation.RequestMapping;
import io.netty.handler.codec.http.HttpMethod;

//Request方法
public class RequestMappingInfo {
    private String path;
    private HttpMethod httpMethod;

    public RequestMappingInfo(String prefix, RequestMapping requestMapping) {
        this.path = prefix + requestMapping.path();
        this.httpMethod = HttpMethod.valueOf(requestMapping.method());
    }

    public String getPath() {
        return path;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

}
