package com.shike.beistmvc.webmvc.http;

import cn.hutool.core.collection.CollectionUtil;
import com.shike.webmvc.http.session.HttpSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.util.concurrent.FastThreadLocal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

public class BeistContext {
    private static final Logger logger = LoggerFactory.getLogger(BeistContext.class);

    /**
     * 使用FastThreadLocal替代JDK自带的ThreadLocal以提升并发性能
     */
    private static final FastThreadLocal<BeistContext> CONTEXT_HOLDER = new FastThreadLocal<>();

    private BeistHttpRequest request;

    private BeistHttpResponse response;

    private ChannelHandlerContext context;

    private HttpSession session;

    private Set<Cookie> cookies;


    private BeistContext(){

    }

    public BeistContext setRequest(BeistHttpRequest request){
        this.request = request;
        return this;
    }

    public void setHandlerContext(ChannelHandlerContext context){
        this.context = context;
    }

    public void setResponse(BeistHttpResponse response){
        this.response = response;
    }

    public BeistHttpResponse getResponse() {
        return response;
    }


    public BeistHttpRequest getRequest() {
        return request;
    }

    public ChannelHandlerContext getHandlerContext() {
        return context;
    }


    public static BeistContext currentContext(){
        BeistContext context = CONTEXT_HOLDER.get();
        if(context==null){
            context = new BeistContext();
            CONTEXT_HOLDER.set(context);
        }
        return context;
    }

    public BeistContext addCookie(Cookie cookie){
        if(cookie!=null){
            if(CollectionUtil.isEmpty(cookies)){
                cookies = new HashSet<>();
            }
            cookies.add(cookie);
        }
        return this;
    }

    public BeistContext addCookies(Set<Cookie> cookieSet){
        if(CollectionUtil.isNotEmpty(cookieSet)){
            if(CollectionUtil.isEmpty(cookies)){
                cookies = new HashSet<>();
            }
            cookies.addAll(cookieSet);
        }
        return this;
    }

    public void setCookies(Set<Cookie> cookies) {
        this.cookies = cookies;
    }

    public HttpSession getSession(){
        return session;
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public static void clear(){
        CONTEXT_HOLDER.remove();
    }
}
