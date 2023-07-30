package com.shike.beistmvc.webmvc.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;

import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

public class BeistHttpResponse implements Serializable {
    private static final long serialVersionUID = -1L;
    private HttpResponseStatus status;
    private HttpVersion version;
    private Charset charset;
    private HttpHeaders headers;
    private File file;
    private ByteBuf content;

    public BeistHttpResponse(HttpResponseStatus status, Charset charset){
        this.version = HttpVersion.HTTP_1_1;
        this.status = status;
        this.charset = charset;
        this.headers = new DefaultHttpHeaders(true);
    }

    public HttpVersion getVersion() {
        return version;
    }

    public void setVersion(HttpVersion version) {
        this.version = version;
    }


    public HttpResponseStatus getStatus() {
        return status;
    }
    public void setStatus(HttpResponseStatus status) {
        this.status = status;
    }

    /**
     * header
     */
    public void addHeader(CharSequence name, Object value) {
        headers.add(name,value);
    }

    public boolean containsHeader(CharSequence name) {
        return headers.contains(name);
    }

    public String getHeader(CharSequence name) {
        return headers.get(name);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
    /**
     * cookie
     */
    public void addCookie(Cookie cookie){
        headers.add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(cookie));
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public String getContent(){
        if (null == content) {
            return null;
        }
        content.markReaderIndex();
        String body = content.toString(charset);
        content.resetReaderIndex();
        return body;
    }

    public ByteBuf content(){
        return content;
    }
    public void setContent(String content){
        setContent(content.getBytes(charset));
    }


    public void setContent(byte[] bytes){
        if(null != bytes){
            if (null == content) {
                content = Unpooled.buffer(bytes.length);
            }else {
                content.clear();
            }
            content.writeBytes(bytes);
        }
    }

    public void writeToContent(byte[] bytes) {
        if(null != bytes){
            if (null == content) {
                content = Unpooled.buffer(bytes.length);
            }
            content.writeBytes(bytes);
        }
    }

    public HttpHeaders headers() {
        return headers;
    }

    public void redirect(String target){
        setStatus(HttpResponseStatus.MOVED_PERMANENTLY);
        addHeader("Location",target);
    }
}
