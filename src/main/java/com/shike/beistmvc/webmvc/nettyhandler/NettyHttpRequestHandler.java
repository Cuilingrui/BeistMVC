package com.shike.beistmvc.webmvc.nettyhandler;

import com.shike.context.ApplicationContext;
import com.shike.webmvc.common.utils.HttpUrlUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

public class NettyHttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    private ApplicationContext appContext;
    private ChannelHandlerContext context;

    NettyHttpRequestHandler(ApplicationContext applicationContext) {
//        this(applicationContext, null);
        this.appContext = applicationContext;
    }

//    NettyHttpRequestHandler(ApplicationContext applicationContext, WebSocketHandlerRegistry registry) {
//        appContext = applicationContext;
//        if (appContext.containsBean(InterceptorRegistry.class.getSimpleName())) {
//            interceptorRegistry = appContext.getBean(InterceptorRegistry.class);
//        }
//        this.webSocketRegistry = registry;
//    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        this.context = ctx;

        processHttpRequest(request);
    }

    private void processHttpRequest(FullHttpRequest request) {
        //如果是Http 100 的请求，
        if (HttpUtil.is100ContinueExpected(request)){
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            context.writeAndFlush(response);
        }

        doProcessHttpRequest(request);
    }

    private void doProcessHttpRequest(FullHttpRequest request) {
        String message = String.format("[%s] %s", request.method(), request.uri());
        String uri = HttpUrlUtil.trimUri(request.uri());


    }
}
