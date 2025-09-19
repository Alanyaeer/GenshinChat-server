package com.homework.chatserver.netty.channel;

import io.netty.channel.Channel;

import java.util.Map;

public interface ChannelManager {
    /**
     * 注册通道
     *
     * @param userId  用户id
     * @param channel 频道
     */
    void registerChannel(String userId, Channel channel);


    /**
     * 取消注册通道
     *
     * @param userId 用户id
     */
    void unRegisterChannel(String userId);

    /**
     * 获取频道
     *
     * @param userId 用户id
     * @return {@link Channel }
     */
    Channel getChannel(String userId);

    /**
     * 获取所有频道
     *
     * @return {@link Channel } {@link [] }
     */
    Map<String, Channel> getChannelMap();
}
