package org.xiaobai.ai.handler.emitter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.xiaobai.ai.api.domain.request.ChatProcessRequest;
import org.xiaobai.ai.api.domain.vo.ChatReplyMessageVO;
import org.xiaobai.ai.entity.ChatUserBalanceVO;
import org.xiaobai.ai.service.ChatConfigService;
import org.xiaobai.ai.service.ChatUserBalanceService;
import org.xiaobai.common.utils.UserUtil;
import org.xiaobai.core.utils.ObjectMapperUtil;

/**
 * @ClassName ChatLimiterEmitterChain
 * @Description Chat 限制额度处理
 * @Author dingfeng.xiao
 * @Date 2023/7/11 16:43
 * @Version 1.0
 */
@Service
public class ChatLimiterEmitterChain extends AbstractResponseEmitterChain {

    private volatile ChatUserBalanceService chatUserBalanceService;
    private volatile ChatConfigService chatConfigService;

    @Override
    public void doChain(ChatProcessRequest request, ResponseBodyEmitter emitter) {
        try {
            this.initBean();
            ChatUserBalanceVO balance = chatUserBalanceService.getBalance(request.getUserId());
            int needBalance = chatConfigService.findChatBalance();
            if (balance.getChatBalance() < needBalance) {
                ChatReplyMessageVO chatReplyMessageVO = ChatReplyMessageVO.onEmitterChainException(request);
                chatReplyMessageVO.setText(StrUtil.format("余额不足，请充值后继续使用！"));
                emitter.send(ObjectMapperUtil.toJson(chatReplyMessageVO));
                emitter.complete();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if (getNext() != null) {
            getNext().doChain(request, emitter);
        }
    }
    private void initBean() {
        if (chatConfigService == null) {
            synchronized (this) {
                if (chatConfigService == null) {
                    chatConfigService = SpringUtil.getBean(ChatConfigService.class);
                }
            }
        }
        if (chatUserBalanceService == null) {
            synchronized (this) {
                if (chatUserBalanceService == null) {
                    chatUserBalanceService = SpringUtil.getBean(ChatUserBalanceService.class);
                }
            }
        }
    }
}
