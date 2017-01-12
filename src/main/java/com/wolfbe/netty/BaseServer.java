package com.wolfbe.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andy
 */
public abstract class BaseServer implements Server {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected DefaultEventLoopGroup defLoopGroup;
    protected NioEventLoopGroup bossGroup;
    protected NioEventLoopGroup workGroup;
    protected NioServerSocketChannel ssch;
    protected ChannelFuture cf;
    protected ServerBootstrap b;

    public abstract void init();
}
