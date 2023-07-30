package com.shike.beistmvc.webmvc.exception;

import com.shike.webmvc.http.BeistHttpRequest;

public class NoHandlerFoundException extends Exception{
    private String httpMethod;
    private String requestURL;

    public NoHandlerFoundException(BeistHttpRequest request) {
        this.httpMethod = request.getHttpMethod().name();
        this.requestURL = request.getRequestUrl();
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public String getRequestURL() {
        return requestURL;
    }
}
