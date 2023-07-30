package com.shike.beistmvc.webmvc.http;

import com.shike.webmvc.http.session.HttpSession;
import com.shike.webmvc.http.session.SessionHelper;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.multipart.FileUpload;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeistHttpRequest implements Serializable {
    private static final long serialVersionUID = -2844694054296931417L;
    private long time;      //请求时间
    private HttpMethod httpMethod;  //GET POST DELETE
    private String requestUrl;  //请求uri
    private HttpVersion httpVersion;
    private HttpHeaders headers;  //header
    private Map<String, List<String>> requestParamsMap; //参数
    private Map<String, FileUpload> requestFiles;

    private ByteBuf body;
    private Map<String, Cookie> cookies; //request cookies
    private Map<String, Object> attachment = new HashMap<>();
    private HttpSession session;

    public BeistHttpRequest() {
    }

    public BeistHttpRequest(HttpVersion httpVersion, HttpHeaders httpHeaders) {
        this.time = System.currentTimeMillis();
        this.headers = httpHeaders;
        this.httpVersion = httpVersion;
    }

    public HttpVersion getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(HttpVersion httpVersion) {
        this.httpVersion = httpVersion;
    }

    public String getParameter(String name) {
        List<String> values = requestParamsMap.get(name);
        if (null != values && values.size() > 0) {
            return values.get(0);
        }
        return null;
    }

    public HttpSession getSession() {
        if (session == null) {
            Cookie sessionCookie = getCookie("sessionid");
            BeistContext ctx = BeistContext.currentContext();
            String sessionId = (sessionCookie == null) ? ctx.getHandlerContext().channel().id().asShortText() : sessionCookie.value();


            SessionHelper sessionHelper = SessionHelper.instance();
            session = sessionHelper.getSession(ctx.getHandlerContext(), true);
        }
        return session;
    }

    public void addParameter(String name, String value) {
        requestParamsMap.put(name, Collections.singletonList(value));
    }

    public List<String> getParameters(String name) {
        return requestParamsMap.get(name);
    }


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public HttpHeaders getHeaders() {
        return headers;
    }

    public void setHeader(CharSequence name, Object value) {
        headers.add(name, value);
    }

    public String getHeader(CharSequence name) {
        return headers.get(name);
    }

    public Map<String, List<String>> getRequestParamsMap() {
        return requestParamsMap;
    }

    public void setRequestParamsMap(Map<String, List<String>> requestParamsMap) {
        this.requestParamsMap = requestParamsMap;
    }

    public FileUpload getFileUpload(String name) {
        if (null == requestFiles) {
            return null;
        }
        return requestFiles.get(name);
    }

    public Map<String, Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, Cookie> cookies) {
        this.cookies = cookies;
    }

    public Cookie getCookie(String name) {
        if (null != cookies) {
            return cookies.get(name);
        }
        return null;
    }

    public Map<String, FileUpload> getRequestFiles() {
        return requestFiles;
    }

    public void setRequestFiles(Map<String, FileUpload> requestFiles) {
        this.requestFiles = requestFiles;
    }

    public void setAttribute(String key, Object value) {

        this.attachment.put(key, value);
    }

    public Object getAttribute(String key) {

        return attachment.get(key);
    }

    public ByteBuf getBody() {
        return body;
    }

    public void setBody(ByteBuf body) {
        this.body = body;
    }

    @Override
    public BeistHttpRequest clone() throws CloneNotSupportedException {
        BeistHttpRequest _request = (BeistHttpRequest) super.clone();
        _request.headers = this.headers;
        _request.httpVersion = this.httpVersion;
        _request.httpMethod = this.httpMethod;
        _request.requestUrl = this.requestUrl;
        _request.body = this.getBody();
        _request.requestParamsMap = new HashMap<>(this.requestParamsMap);
        _request.cookies = new HashMap<>(this.cookies);
        _request.attachment = new HashMap<>(this.attachment);
        return _request;
    }
}
