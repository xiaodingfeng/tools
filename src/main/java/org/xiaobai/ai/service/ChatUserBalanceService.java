package org.xiaobai.ai.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaobai.ai.entity.ChatUserBalanceVO;
import org.xiaobai.ai.response.DayBalanceResponse;

/**
 * 用户额度管理
 */
public interface ChatUserBalanceService extends IService<ChatUserBalanceVO>  {
    /**
     * 获取用户额度
     * @return
     */
    ChatUserBalanceVO getBalance(Long userId);
    /**
     * 每日领取的额度
     * @return
     */
    DayBalanceResponse dayBalance(Long userId);

    /**
     * 领取额度
     * @return
     */
    Boolean receiveBalance(Long userId);

    /**
     * 用户额度消耗 balance
     * @param userId
     * @return
     */
    Boolean reduceChatBalance(Long userId, Integer balance);

    /**
     * 增加额度
     * @param userId
     * @param balance
     * @return
     */
    Boolean increaseChatBalance(Long userId, Integer balance);

    /**
     * 自动消耗额度
     * @param userId
     * @return
     */
    Boolean autoReduceChatBalance(Long userId);

    /**
     * 初始化新用户额度信息
     * @param userId
     */
    Boolean initUserBalance(Long userId);
}
