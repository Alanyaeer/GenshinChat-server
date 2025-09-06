package com.homework.chatserver.netty.channel;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 单机版通道
 *
 * @author wps
 * @date 2025/09/04
 */
public class SingletonChannelManager implements ChannelManager {
    public static final SingletonChannelManager INSTANCE = new SingletonChannelManager();

    private SingletonChannelManager(){}
    /**
     * 通道映射
     */
    private static final Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    /**
     * 注册通道
     *
     * @param userId  用户id
     * @param channel 频道
     */
    public void registerChannel(String userId, Channel channel){
        if(!channel.isActive()){
            throw new RuntimeException("连接断开");
        }
        channelMap.put(userId, channel);
    }

    /**
     * 取消注册通道
     *
     * @param userId 用户id
     */
    public void unRegisterChannel(String userId){
        channelMap.remove(userId);
    }

    /**
     * 获取频道
     *
     * @param userId 用户id
     * @return {@link Channel }
     */
    public Channel getChannel(String userId){
        return channelMap.get(userId);
    }
}
