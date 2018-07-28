package com.rusya.chat.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;

/**
 * Created by rusJA
 */
public class ChatClientHandler extends ChannelInboundMessageHandlerAdapter<String> {
    public void messageReceived(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println(s);
    }
}
