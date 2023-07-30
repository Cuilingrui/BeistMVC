package com.shike.beistmvc.webmvc.handler.mapping;


import com.shike.webmvc.handler.HandlerExecutionChain;
import com.shike.webmvc.http.BeistHttpRequest;

public interface HandlerMapping {

    HandlerExecutionChain getHandler(BeistHttpRequest request) throws Exception;

}
