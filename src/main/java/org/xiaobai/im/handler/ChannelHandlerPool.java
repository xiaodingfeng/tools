package org.xiaobai.im.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelGroupFuture;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.xiaobai.im.entity.ChatGroupRecordVO;
import org.xiaobai.im.request.ChatMessage;
import org.xiaobai.im.service.ChatGroupRecordService;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ChannelHandlerPool {

    private static final ChannelHandlerPool POOL = new ChannelHandlerPool();
    /**
     * map: userId,ChannelId
     */
    private static final Map<String, ChannelId> CHANNEL_ID_MAP = new ConcurrentHashMap<>();
    /**
     * 管道组
     */
    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private final ChatGroupRecordService chatGroupRecordService = SpringUtil.getBean(ChatGroupRecordService.class);
    private final ThreadPoolTaskExecutor executor = SpringUtil.getBean(ThreadPoolTaskExecutor.class);

    //private final MessageConverter messageConverter = SpringUtil.getBean(MessageConverter.class);
    private ChannelHandlerPool() {
    }

    public static ChannelHandlerPool pool() {
        return POOL;
    }

    ChatGroupRecordVO messageToMsgSocket(ChatMessage message) {
        ChatGroupRecordVO recordVO = new ChatGroupRecordVO();
        recordVO.setContent(JSON.toJSONString(message.getMessage()));
        recordVO.setUserId(Long.valueOf(message.getFromUserId()));
        recordVO.setIpAddress(message.getIpAddress());
        return recordVO;
    }

    public Map<String, ChannelId> getChannelIdMap() {
        return CHANNEL_ID_MAP;
    }

    public ChannelGroup getChannelGroup() {
        return CHANNEL_GROUP;
    }


    /**
     * 获取channelId
     *
     * @param userId 用户id
     * @return /
     */
    public ChannelId getChannelId(String userId) {
        if (Objects.isNull(userId)) {
            return null;
        }
        return CHANNEL_ID_MAP.get(userId);
    }


    /**
     * 获取channel
     *
     * @param channelId /
     * @return /
     */
    public Channel getChannel(ChannelId channelId) {
        return CHANNEL_GROUP.find(channelId);
    }

    public Channel getChannel(String userId) {
        return CHANNEL_GROUP.find(getChannelId(userId));
    }

    public void removeChannelId(String userid) {
        if (StrUtil.isNotBlank(userid)) {
            CHANNEL_ID_MAP.remove(userid);
        }
    }

    public void removeChannel(Channel channel) {
        CHANNEL_GROUP.remove(channel);
    }

    public void addChannel(Channel channel) {
        CHANNEL_GROUP.add(channel);
    }

    /**
     * 群发
     *
     * @param message 消息内容
     */
    public void writeAndFlush(ChatMessage message) {
        final ChannelGroupFuture channelFutures = CHANNEL_GROUP.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(message)));

    }

    /**
     * 私发
     *
     * @param channel channel
     * @param message 内容
     */
    public void writeAndFlush(Channel channel, ChatMessage message) {
        channel.writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(message)));
    }

    /**
     * 私发
     *
     * @param channelId channelId
     * @param message   消息
     */
    public void writeAndFlush(ChannelId channelId, ChatMessage message) {
        findChannel(channelId).writeAndFlush(new TextWebSocketFrame(JSONObject.toJSONString(message)));
    }

    public Channel findChannel(ChannelId channelId) {
        return CHANNEL_GROUP.find(channelId);
    }

    public void putChannelId(String userid, ChannelId channelId) {
        CHANNEL_ID_MAP.put(userid, channelId);
    }

    public void saveBase(ChatMessage message) {
        log.info("暂未实现");
    }

    public void saveGroupBase(ChatMessage message) {
        executor.submit(() -> {
            String name = Thread.currentThread().getName();
            Long id = Thread.currentThread().getId();
            log.info("message:{},线程名称：{},id:{}", message, name, id);
            chatGroupRecordService.addMessage(messageToMsgSocket(message));
        });
    }
}
