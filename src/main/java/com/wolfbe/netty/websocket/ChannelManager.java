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
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Channel的管理器
 *
 * @author laochunyu
 */
public class ChannelManager {
    private static final Logger logger = LoggerFactory.getLogger(ChannelManager.class);

    private static ConcurrentMap<Channel, Long> channels = new ConcurrentHashMap<>();
    private static ReadWriteLock rwLock = new ReentrantReadWriteLock(true);


    public static void addChannel(Channel channel, long time) {
        if(!channel.isActive()){
            InetSocketAddress saddr = (InetSocketAddress) channel.remoteAddress();
            logger.error("channel is not active,host: {}, port: {}",saddr.getHostName(),saddr.getPort());
        }
        channels.put(channel, time);
    }

    /**
     * 从缓存中移除Channel，并且关闭Channel
     * @param channel
     */
    public static void removeChannel(Channel channel) {
        try {
            rwLock.writeLock().lock();
            channel.close();
            channels.remove(channel);
        }finally {
            rwLock.writeLock().unlock();
        }

    }

    public static void broadcast(String message) {
        if (!BlankUtil.isBlank(message)) {
            try {
                rwLock.readLock().lock();
                Set<Channel> keySet = channels.keySet();
                for (Channel ch : keySet) {
                    ch.writeAndFlush(new TextWebSocketFrame(message));
                }
            }finally {
                rwLock.readLock().unlock();
            }
        }
    }

    public static ConcurrentMap<Channel, Long> getChannels() {
        try {
            rwLock.readLock().lock();
            return channels;
        }finally {
            rwLock.readLock().unlock();
        }
    }
}
