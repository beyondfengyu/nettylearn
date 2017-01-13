package com.wolfbe.netty.websocket;

import com.wolfbe.netty.util.BlankUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author laochunyu
 */
public class ChannelManage {
    private static final Logger logger = LoggerFactory.getLogger(ChannelManage.class);

    private static ConcurrentMap<Channel, Long> channels = new ConcurrentHashMap<>();

    public static ConcurrentMap<Channel, Long> getChannels() {
        return channels;
    }

    public static void addChannel(Channel channel, long time) {
        if(!channel.isActive()){
            InetSocketAddress saddr = (InetSocketAddress) channel.remoteAddress();
            logger.error("channel is not active,host: {}, port: {}",saddr.getHostName(),saddr.getPort());
        }
        channels.put(channel, time);
    }

    public static void removeChannel(Channel channel) {
        channels.remove(channel);
    }

    public static void broadcast(String message) {
        if (!BlankUtil.isBlank(message)) {
            Set<Channel> keySet = channels.keySet();
            for (Channel ch : keySet) {
                ch.writeAndFlush(new TextWebSocketFrame(message));
            }
        }
    }

}
