package org.xiaobai.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaobai.ai.entity.ChatConfigVO;

/**
 * chat 配置
 */
public interface ChatConfigService extends IService<ChatConfigVO> {
    /**
     * key 获取配置信息
     * @param key
     * @return
     */
    String findByKey(String key);

    /**
     * 获取一次chat 消耗额度
     * @return
     */
    int findChatBalance();
}
