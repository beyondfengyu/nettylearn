package com.wolfbe.netty.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WebSocket聊天室，客户端参考docs目录下的websocket.html
 * @author Andy
 */
public class WebSocketMain {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketMain.class);

    public static void main(String[] args) {
        final WebSocketServer server = new WebSocketServer(8099);
        server.init();
        server.start();
        // 注册进程钩子，在JVM进程关闭前释放资源
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                server.shutdown();
                logger.warn(">>>>>>>>>> jvm shutdown");
                System.exit(0);
            }
        });
    }
}
