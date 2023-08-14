package org.xiaobai.im.handler;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.xiaobai.ai.api.domain.request.ChatProcessRequest;
import org.xiaobai.ai.service.ChatMessageService;
import org.xiaobai.ai.service.ChatRoomService;
import org.xiaobai.common.constants.SystemConstant;
import org.xiaobai.common.service.SysUserService;
import org.xiaobai.core.utils.IpUtil;
import org.xiaobai.core.utils.WebUtil;
import org.xiaobai.im.enums.ChatTypeEnum;
import org.xiaobai.im.request.ChatMessage;

import java.util.Objects;

import static org.xiaobai.im.utils.AttributeKeyUtils.SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY;

/**
 * 基础消息处理器
 */
@Slf4j
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private final SysUserService sysUserService = SpringUtil.getBean(SysUserService.class);
    private final ChatRoomService chatRoomService = SpringUtil.getBean(ChatRoomService.class);
    private final ChatMessageService chatMessageService = SpringUtil.getBean(ChatMessageService.class);

    /**
     * 连接时
     *
     * @param ctx 上下文
     * @throws Exception /
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("与客户端建立连接，通道开启！");
        // 添加到channelGroup通道组
        ChannelHandlerPool.pool().addChannel(ctx.channel());
    }

    /**
     * 断开连接时
     *
     * @param ctx 上下文
     * @throws Exception /
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("与客户端断开连接，通道关闭！");
        // 从channelGroup通道组移除
        ChannelHandlerPool.pool().removeChannel(ctx.channel());
        SecurityServerHandler.SecurityCheckComplete complete = ctx.channel().attr(SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY).get();
        if (Objects.isNull(complete)) {
            return;
        }
        String useridQuit = complete.getUserId();
        ChannelHandlerPool.pool().removeChannelId(useridQuit);
        log.info("断开的用户id为：{}", useridQuit);
        ChatMessage response = new ChatMessage();
        response.setFromUserId(useridQuit);
        response.setType(ChatTypeEnum.CHAT_USER_DOWN_LINE.getType());
        handlerConvertUser(response, ctx);
        sendOther(ctx, response);
    }

    /**
     * 获取消息时
     *
     * @param ctx 上下文
     * @param msg 消息
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
        ChatMessage message = null;
        try {
            message = JSON.parseObject(msg.text(), ChatMessage.class);
        } catch (Exception e) {
            ChatMessage response = new ChatMessage();
            response.setType(ChatTypeEnum.CHAT_MSG_ERROR.getType());
            response.setMessage(ChatMessage.Message.builder().content("消息格式错误").type("text").build());
            sendMsg(ctx, response);
            handlerConvertUser(response, ctx);
            return;
        }
        String userId = ctx.channel().attr(SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY).get().getUserId();
        if (ChatTypeEnum.CHAT_USER_ONLINE.getType().equals(message.getType())) {
            ChatMessage response = new ChatMessage();
            response.setFromUserId(userId);
            response.setType(message.getType());
            handlerConvertUser(response, ctx);
            sendAllMessage(response);
        }
        if (ChatTypeEnum.CHAT_USER_USER.getType().equals(message.getType())) {
            ChatMessage response = new ChatMessage();
            response.setFromUserId(userId);
            response.setToUserId(message.getToUserId());
            response.setType(message.getType());
            response.setMessage(message.getMessage());
            handlerConvertUser(response, ctx);
            sendMsg(response);
        }
        if (ChatTypeEnum.CHAT_USER_GROUP.getType().equals(message.getType())) {
            ChatMessage response = new ChatMessage();
            response.setFromUserId(userId);
            response.setToUserId(message.getToUserId());
            response.setType(message.getType());
            response.setMessage(message.getMessage());
            handlerConvertUser(response, ctx);
            sendGroupOther(ctx, response);

            ChatMessage.Message content = message.getMessage();
            String contentContent = content.getContent();
            if (content.getType().equals("text") && contentContent.replace("<p>", "").startsWith("@chat")) {
                createChatMessage(ctx, message, userId, response, content, contentContent);
            }
        }
    }

    private void createChatMessage(ChannelHandlerContext ctx, ChatMessage message, String userId, ChatMessage response, ChatMessage.Message content, String contentContent) {
        String chatContent = contentContent.replace("@chat", "").trim();

        ChatProcessRequest chatProcessRequest = new ChatProcessRequest();
        chatProcessRequest.setPrompt(chatContent);
        chatProcessRequest.setUserId(Long.valueOf(userId));
        ChatProcessRequest.Options options = new ChatProcessRequest.Options();
        options.setRoomId(chatRoomService.createDefaultRoom());
        chatProcessRequest.setOptions(options);

        // 消息接收结束后，发送给全员
        chatProcessRequest.addFinishConsumer(replyChat -> {
            ChatMessage chatResponse = new ChatMessage();
            chatResponse.setFromUserId(SystemConstant.USER_CHAT_ID);
            chatResponse.setToUserId(null);
            chatResponse.setType(message.getType());
            content.setContent("@" + response.getUser().getNickName() + "\n  " + replyChat);
            chatResponse.setMessage(content);
            handlerConvertUser(chatResponse, ctx);
            sendAllMessage(chatResponse);
        });
        chatMessageService.sendWebSocketMessage(chatProcessRequest);
    }

    private void handlerConvertUser(ChatMessage response, ChannelHandlerContext ctx) {
        SecurityServerHandler.SecurityCheckComplete complete = ctx.channel().attr(SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY).get();
        if (Objects.isNull(complete)) {
            return;
        }
        response.setIpAddress(WebUtil.getSocketIp(complete.getRequestHeaders(), ctx));
        response.setIpSource(IpUtil.searchIpCity(response.getIpAddress()));
        response.setUser(sysUserService.getById(response.getFromUserId()));
    }

    /**
     * 添加channel 回调方法
     *
     * @param ctx /
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        //打印出channel唯一值，asLongText方法是channel的id的全名
        log.info("handlerAdded :{}", ctx.channel().id().asLongText());
    }

    /**
     * 删除channel 回调方法
     *
     * @param ctx /
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        log.info("handlerRemoved :{}", ctx.channel().id().asLongText());
    }

    /**
     * 时间监听器
     *
     * @param ctx /
     * @param evt /
     * @throws Exception /
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof SecurityServerHandler.SecurityCheckComplete) {
            log.info("Security check has passed");
            // 鉴权成功后的逻辑 暂不添加
        } else if (evt instanceof CustomWebSocketServerProtocolHandler.HandshakeComplete) {
            log.info("Handshake has completed");
            SecurityServerHandler.SecurityCheckComplete securityCheckComplete = ctx.channel().attr(SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY).get();
            Boolean hasToken = securityCheckComplete.getHasToken();
            log.info("Handshake has completed after check hasToken:{}", hasToken);
            // 握手成功后的逻辑  如果鉴权了就绑定channel
            if (hasToken) {
                log.info("Handshake has completed after binding channel");
                binding(ctx, securityCheckComplete.getUserId());
                ChatMessage response = new ChatMessage();
                response.setFromUserId(securityCheckComplete.getUserId());
                response.setType(ChatTypeEnum.CHAT_USER_ONLINE.getType());
                handlerConvertUser(response, ctx);
                sendOther(ctx, response);
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("exceptionCaught 异常:{}", cause.getMessage());
        cause.printStackTrace();
        Channel channel = ctx.channel();
        //……
        if (channel.isActive()) {
            log.info("手动关闭通道");
            ctx.close();
        }
    }

    /**
     * 群发所有人
     *
     * @param message 消息
     */
    private void sendAllMessage(ChatMessage message) {
        ChannelHandlerPool.pool().saveGroupBase(message);
        //收到信息后，群发给所有channel
        ChannelHandlerPool.pool().writeAndFlush(message);
    }

    /**
     * 发送消息
     *
     * @param ctx     上下文
     * @param message 消息
     */
    private void sendMsg(ChannelHandlerContext ctx, ChatMessage message) {
        //给自己发自己的消息
        ChannelHandlerPool.pool().writeAndFlush(ctx.channel().id(), message);
    }

    /**
     * 绑定channel与userid
     *
     * @param ctx     上下文
     * @param message 消息
     */
    public void binding(ChannelHandlerContext ctx, ChatMessage message) {
        ChannelId channelId = ctx.channel().id();
        Channel channel = ChannelHandlerPool.pool().getChannel(channelId);
        // 查看是否存在当前channel，不存在便重新插入
        if (null == channel) {
            ChannelHandlerPool.pool().addChannel(ctx.channel());
        }
        try {
            //绑定userid 与 channel
            ChannelHandlerPool.pool().putChannelId(message.getFromUserId(), channelId);
        } catch (Exception e) {
            log.info("主动断开");
            e.printStackTrace();
            // 发生异常断开连接
            ctx.close();
        }
    }

    /**
     * 绑定channel与userid
     *
     * @param ctx    上下文
     * @param userId 用户id
     */
    public void binding(ChannelHandlerContext ctx, String userId) {
        ChannelId channelId = ctx.channel().id();
        Channel channel = ChannelHandlerPool.pool().getChannel(channelId);
        // 查看是否存在当前channel，不存在便重新插入
        if (null == channel) {
            ChannelHandlerPool.pool().addChannel(ctx.channel());
        }
        try {
            SecurityServerHandler.SecurityCheckComplete complete = ctx.channel().attr(SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY).get();
            //绑定userid 与 channel
            ChannelHandlerPool.pool().putChannelId(complete.getHasToken() ? complete.getUserId() : userId, channelId);
        } catch (Exception e) {
            log.info("主动断开");
            e.printStackTrace();
            // 发生异常断开连接
            ctx.close();
        }
    }

    public void sendMsg(ChatMessage message) {
        //私聊
        ChannelId channelId = ChannelHandlerPool.pool().getChannelId(message.getToUserId());
        if (null == channelId) {
            log.info("用户： {}，已经下线!", message.getToUserId());
            //下线操作 存库
            return;
        }
        Channel channel = ChannelHandlerPool.pool().getChannel(channelId);
        if (null == channel) {
            log.info("清除用户：{}在mapper存的channelId", message.getToUserId());
            //特殊下线两个静态变量值不对称处理
            ChannelHandlerPool.pool().removeChannelId(message.getFromUserId());
            return;
        }
        ChannelHandlerPool.pool().writeAndFlush(channel, message);
        log.info("channel中的userId:{}", channel.attr(SECURITY_CHECK_COMPLETE_ATTRIBUTE_KEY).get().getUserId());
    }

    /**
     * 发送给除了自己的其他人
     *
     * @param ctx     上下文
     * @param message 消息
     */
    public void sendOther(ChannelHandlerContext ctx, ChatMessage message) {
        for (Channel channel : ChannelHandlerPool.pool().getChannelGroup()) {
            //给除自己外的人发消息
            if (channel != ctx.channel()) {
                log.info("发送消息：{}", message);
                ChannelHandlerPool.pool().writeAndFlush(channel, message);
            }
        }
    }

    /**
     * 发送给除了自己的其他人
     *
     * @param ctx     上下文
     * @param message 消息
     */
    public void sendGroupOther(ChannelHandlerContext ctx, ChatMessage message) {
        ChannelHandlerPool.pool().saveGroupBase(message);
        for (Channel channel : ChannelHandlerPool.pool().getChannelGroup()) {
            //给除自己外的人发消息
            if (channel != ctx.channel()) {
                log.info("发送消息：{}", message);
                ChannelHandlerPool.pool().writeAndFlush(channel, message);
            }
        }
    }
}

