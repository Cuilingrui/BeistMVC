package com.shike.beistmvc.webmvc.nettyhandler;

import com.shike.webmvc.common.utils.HttpRequestUtil;
import com.shike.webmvc.http.BeistHttpRequest;
import com.shike.webmvc.http.BeistHttpResponse;
import com.shike.webmvc.common.utils.HttpUrlUtil;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class BeistHttpCodec extends MessageToMessageCodec<FullHttpRequest, BeistHttpResponse> {


    private final Logger logger = LoggerFactory.getLogger(BeistHttpCodec.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, BeistHttpResponse msg, List<Object> out) throws Exception {

        DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, msg.getStatus());

        response.headers().add(msg.headers());
        out.add(response);
        if (msg.content() != null) {
            out.add(new DefaultHttpContent(msg.content()));
        }
        out.add(LastHttpContent.EMPTY_LAST_CONTENT);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) throws Exception {
        if (msg.decoderResult().isFailure()) {
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
            logger.debug("illegal http, address={}", ctx.channel().remoteAddress());

            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            return;
        }

        //convert to request
        BeistHttpRequest request = new BeistHttpRequest(msg.protocolVersion(), msg.headers());
        //访问时间
        request.setTime(System.currentTimeMillis());
        //method
        request.setHttpMethod(msg.method());
        //url
        request.setRequestUrl(HttpUrlUtil.trimUri(msg.uri()));

        //content
        request.setBody(msg.content());

        HttpRequestUtil.fillParamsMap(msg, request);     //init http params
        HttpRequestUtil.fillCookies(msg, request);      //init http cookie
        out.add(request);
    }

    private SimpleDateFormat getDateFormatter() {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.CHINA);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormatter;
    }
}
