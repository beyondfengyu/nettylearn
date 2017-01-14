package com.wolfbe.netty.websocket.handler;

import com.wolfbe.netty.websocket.entity.UserInfo;
import com.wolfbe.netty.websocket.proto.ChatCode;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andy
 */
public class MessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame)
            throws Exception {

        UserInfo userInfo = UserInfoManager.getUserInfo(ctx.channel());
        if (userInfo != null) {
            // 广播返回用户发送的消息文本
            UserInfoManager.broadcastMess(userInfo.getNick(), ((TextWebSocketFrame) frame).text());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("connection error and close the channel", cause);
        UserInfoManager.removeChannel(ctx.channel());
        UserInfoManager.broadCastInfo(ChatCode.SYS_USER_COUNT, UserInfoManager.getAuthUserCount());
    }

}
