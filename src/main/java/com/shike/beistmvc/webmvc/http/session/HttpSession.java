package com.shike.beistmvc.webmvc.http.session;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;

import java.util.HashMap;
import java.util.Map;

public class HttpSession {

    /**
     * 会话id
     */
    private ChannelId id;

    /**
     * 会话保存的ChannelHandlerContext
     */
    private ChannelHandlerContext context;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 过期时间
     * 每次请求时都更新过期时间
     */
    private Long expireTime;

    /**
     * Session中存储的数据
     */
    private Map<String, Object> sessionMap;


    private void assertSessionMapNotNull() {
        if (sessionMap == null) {
            sessionMap = new HashMap<>();
        }
    }

    private HttpSession() {

    }


    //=====================================

    public HttpSession(ChannelHandlerContext context) {
        this(context.channel().id(), context);
    }

    public HttpSession(ChannelId id, ChannelHandlerContext context) {
        this(id, context, System.currentTimeMillis());
    }

    public HttpSession(ChannelId id, ChannelHandlerContext context, Long createTime) {
        this(id, context, createTime, createTime + SessionConfig.instance().sessionTimeOut());
    }

    public HttpSession(ChannelId id, ChannelHandlerContext context, Long createTime, Long expireTime) {
        this.id = id;
        this.context = context;
        this.createTime = createTime;
        this.expireTime = expireTime;
        assertSessionMapNotNull();
    }

    public ChannelId getId() {
        return id;
    }

    public void setId(ChannelId id) {
        this.id = id;
    }

    public ChannelHandlerContext getContext() {
        return context;
    }

    public void setContext(ChannelHandlerContext context) {
        this.context = context;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * 是否过期
     *
     * @return
     */
    public boolean isExpire() {
        return this.expireTime >= System.currentTimeMillis();
    }

    /**
     * 设置attribute
     *
     * @param key
     * @param val
     */
    public void setAttribute(String key, Object val) {
        sessionMap.put(key, val);
    }

    /**
     * 获取key的值
     *
     * @param key
     */
    public Object getAttribute(String key) {
        return sessionMap.get(key);
    }

    /**
     * 是否存在key
     *
     * @param key
     */
    public boolean containsAttribute(String key) {
        return sessionMap.containsKey(key);
    }


}
