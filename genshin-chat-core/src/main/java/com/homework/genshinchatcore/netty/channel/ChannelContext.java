package com.homework.genshinchatcore.netty.channel;

import com.homework.chatserver.netty.channel.ChannelManager;
import com.homework.chatserver.netty.channel.SingletonChannelManager;

public class ChannelContext {
    private static ChannelManager defaultChannel;
    public static final ChannelContext SELECTOR = new ChannelContext();

    static{
        defaultChannel = SingletonChannelManager.INSTANCE;
    }
    public static ChannelManager select(){
        return defaultChannel;
    }
}
