package org.xiaobai.ai.vip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaobai.ai.vip.entity.ChatFreeConfigVO;
import org.xiaobai.ai.vip.entity.ChatOrderVO;
import org.xiaobai.ai.vip.mapper.ChatFreeConfigMapper;
import org.xiaobai.ai.vip.mapper.ChatOrderMapper;
import org.xiaobai.ai.vip.service.ChatFreeConfigService;
import org.xiaobai.ai.vip.service.ChatOrderService;
import org.xiaobai.core.enums.ValidEnum;

/**
 * @ClassName ChatFreeConfigServiceImpl
 * @Author dingfeng.xiao
 * @Date 2023/7/11 16:40
 * @Version 1.0
 */
@Service
public class ChatFreeConfigServiceImpl extends ServiceImpl<ChatFreeConfigMapper, ChatFreeConfigVO> implements ChatFreeConfigService {

    @Override
    public ChatFreeConfigVO findValidConfig() {
        return getOne(new LambdaQueryWrapper<ChatFreeConfigVO>()
        .eq(ChatFreeConfigVO::getValid, ValidEnum.VALID));
    }
}
