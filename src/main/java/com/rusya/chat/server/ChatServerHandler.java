package com.rusya.chat.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.Channel;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.net.InetAddress;

/**
 * Created by rusJA
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {


    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
        ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
                new GenericFutureListener<Future<Channel>>() {
                    public void operationComplete(Future< Channel> future) throws Exception {
                        ctx.writeAndFlush("Welcome to "+ InetAddress.getLocalHost().getHostName() +
                        " chat service!\n");
                        ctx.writeAndFlush("Your session is protected by "+
                        ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite()+
                        " ciper suite.\n");

                        channels.add(ctx.channel());

                    }
                }
        );

    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        for (Channel c : channels) {
            if (c != channelHandlerContext.channel()) {
                c.writeAndFlush("[" + channelHandlerContext.channel().remoteAddress() + "] " + s + '\n');
            } else {
                c.writeAndFlush("[you] " + s + '\n');
            }
            if("bye".equals((s.toLowerCase()))){
                channelHandlerContext.close();
            }
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}