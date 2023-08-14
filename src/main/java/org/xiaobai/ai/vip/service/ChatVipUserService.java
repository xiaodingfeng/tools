package org.xiaobai.ai.vip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaobai.ai.vip.entity.ChatOrderVO;
import org.xiaobai.ai.vip.entity.ChatVipUserVO;

import java.util.List;

/**
 * @ClassName ChatVipUserService
 * @Description 用户会员
 * @Author dingfeng.xiao
 * @Date 2023/7/10 10:06
 * @Version 1.0
 */
public interface ChatVipUserService extends IService<ChatVipUserVO> {
    /**
     * 用户充值完新增vip
     * @param orderVO
     * @return
     */
    Boolean addUserVip(ChatOrderVO orderVO);

    /**
     * 获取用户vip信息
     * @param userId
     * @return
     */
    List<ChatVipUserVO> getUserVip(Long userId);
}
