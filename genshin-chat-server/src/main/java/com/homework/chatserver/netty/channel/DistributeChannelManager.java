package com.homework.chatserver.netty.channel;

import io.netty.channel.Channel;

import java.util.Map;

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

    @Override
    public Map<String, Channel> getChannelMap() {
        return null;
    }
}
