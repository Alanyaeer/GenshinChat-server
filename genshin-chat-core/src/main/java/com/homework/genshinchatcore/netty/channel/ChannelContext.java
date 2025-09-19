package com.homework.genshinchatcore.netty.channel;


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
