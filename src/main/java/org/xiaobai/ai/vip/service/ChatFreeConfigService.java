package org.xiaobai.ai.vip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.binarywang.wxpay.exception.WxPayException;
import org.xiaobai.ai.vip.entity.ChatFreeConfigVO;
import org.xiaobai.ai.vip.entity.ChatOrderVO;
import org.xiaobai.ai.vip.request.ChatVipOrderAddRequest;
import org.xiaobai.ai.vip.response.ChatVipOrderAddResponse;
import org.xiaobai.core.enums.OrderStatusEnum;

import java.io.IOException;

/**
 * @ClassName ChatFreeConfigService
 * @Description 免费额度配置
 * @Author dingfeng.xiao
 * @Date 2023/7/10 10:06
 * @Version 1.0
 */
public interface ChatFreeConfigService extends IService<ChatFreeConfigVO> {
    /**
     * 获取有效的配置
     * @return 配置信息
     */
    ChatFreeConfigVO findValidConfig();
}
