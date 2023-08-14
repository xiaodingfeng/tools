package org.xiaobai.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.xiaobai.ai.constants.ChatConfigConstant;
import org.xiaobai.ai.entity.ChatConfigVO;
import org.xiaobai.ai.mapper.ChatConfigMapper;
import org.xiaobai.ai.service.ChatConfigService;
import org.xiaobai.core.enums.ValidEnum;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * chat 配置
 */
@Service
public class ChatConfigServiceImpl extends ServiceImpl<ChatConfigMapper, ChatConfigVO> implements ChatConfigService {

    private final Map<String, String> CACHE_MAP = new ConcurrentHashMap<>();

    @PostConstruct
    public void initConfig() {
        try {
            List<ChatConfigVO> list = list(new LambdaQueryWrapper<ChatConfigVO>()
                    .eq(ChatConfigVO::getValid, ValidEnum.VALID));
            for (ChatConfigVO configVO : list) {
                CACHE_MAP.put(configVO.getConfigKey(), configVO.getConfigValue());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public String findByKey(String key) {
        String cache = CACHE_MAP.get(key);
        if (StringUtils.hasText(cache)) {
            return cache;
        }
        ChatConfigVO configVO = getOne(new LambdaQueryWrapper<ChatConfigVO>()
                .eq(ChatConfigVO::getValid, ValidEnum.VALID)
                .eq(ChatConfigVO::getConfigKey, key));
        return configVO == null ? null : configVO.getConfigValue();
    }

    @Override
    public int findChatBalance() {
        final String balance = findByKey(ChatConfigConstant.CHAT_NEED_BALANCE);
        return StringUtils.hasText(balance) ? Integer.parseInt(balance) : 0;
    }
}
