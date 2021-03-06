package com.rusya.chat.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;


/**
 * Created by rusJA
 */
public class ChatServerInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public ChatServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();

        if (sslCtx != null){
            pipeline.addLast(sslCtx.newHandler(socketChannel.alloc()));
        }


        pipeline.addLast( new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast("decoder",new StringDecoder());
        pipeline.addLast("encoder", new StringEncoder());

        pipeline.addLast("handler",new ChatServerHandler());
    }
}
