package com.homework.genshinchat.netty.channel;

import io.netty.channel.Channel;

public class DistributeChannelManager implements ChannelManager{
    @Override
    public void registerChannel(String userId, Channel channel) {

    }

    @Override
    public void unRegisterChannel(String userId) {

    }

    @Override
    public Channel getChannel(String userId) {
        return null;
    }
}
