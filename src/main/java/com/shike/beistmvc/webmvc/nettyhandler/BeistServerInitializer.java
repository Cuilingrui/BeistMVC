package com.shike.beistmvc.webmvc.nettyhandler;

import com.shike.context.ApplicationContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BeistServerInitializer extends ChannelInitializer<SocketChannel> {
    private static final Logger logger = LoggerFactory.getLogger(BeistServerInitializer.class);
    private final ApplicationContext context;

    public BeistServerInitializer(ApplicationContext context){
        this.context = context;
    }
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //超时设置
//        pipeline.addLast("idleStateHandler", new IdleStateHandler(0, 0,config.getKeepAliveTimeout()));
        //http编解码器
        pipeline.addLast("httpCodec", new HttpServerCodec());
        //负责多个chunk的http 请求和响应的
        pipeline.addLast("aggregator", new HttpObjectAggregator(512 * 1024));
        pipeline.addLast("chunk",new ChunkedWriteHandler());
        //自定义http解析器，把原生的HttpRequest包装一下
        pipeline.addLast("beistHttpCodec",new BeistHttpCodec());
        pipeline.addLast("beistHttpCodec",new BeistHttpCodec());
    }

}
