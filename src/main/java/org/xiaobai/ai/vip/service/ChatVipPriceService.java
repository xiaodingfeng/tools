package org.xiaobai.ai.vip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.xiaobai.ai.vip.entity.ChatVipPriceVO;

import java.util.List;

/**
 * @ClassName ChatVipPriceService
 * @Description 会员信息
 * @Author dingfeng.xiao
 * @Date 2023/7/10 10:06
 * @Version 1.0
 */
public interface ChatVipPriceService extends IService<ChatVipPriceVO> {
    /**
     * 获取正在生效vip信息
     * @param id
     */
    ChatVipPriceVO getValidVipPrice(Long id);

    /**
     * vipId 获取正在生效vip信息
     * @param vipId
     */
    List<ChatVipPriceVO> listValidVipPrice(Long vipId);
}
