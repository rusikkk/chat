package com.rusya.chat.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

/**
 * Created by rusJA
 */
public class ChatServerHandler extends ChannelInboundMessageHandlerAdapter<String> {

    private static final ChannelGroup channels = new DefaultChannelGroup();

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.write("[SERVER] - " + incoming.remoteAddress() + "has joined\n");
        }
        channels.add(ctx.channel());

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.write("[SERVER] - " + incoming.remoteAddress() + "has left\n");
        }
        channels.remove(ctx.channel());

    }


    public void messageReceived(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel incoming = channelHandlerContext.channel();
        for (Channel channel: channels){
            if(channel != incoming){
                channel.write("[" + incoming.remoteAddress() + "]" + s + "\n");
            }
        }
    }
}