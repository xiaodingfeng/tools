package org.xiaobai.ai.vip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xiaobai.ai.vip.entity.ChatVipPriceVO;
import org.xiaobai.core.enums.ValidEnum;
import org.xiaobai.ai.vip.mapper.ChatVipPriceMapper;
import org.xiaobai.ai.vip.service.ChatVipPriceService;

import java.util.List;

/**
 * @ClassName ChatVipPriceServiceImpl
 * @Author dingfeng.xiao
 * @Date 2023/7/10 10:07
 * @Version 1.0
 */
@Service
public class ChatVipPriceServiceImpl extends ServiceImpl<ChatVipPriceMapper, ChatVipPriceVO> implements ChatVipPriceService {
    @Override
    public ChatVipPriceVO getValidVipPrice(Long id) {
        return getOne(new LambdaQueryWrapper<ChatVipPriceVO>()
                .eq(ChatVipPriceVO::getValid, ValidEnum.VALID)
                .eq(ChatVipPriceVO::getId, id));
    }

    @Override
    public List<ChatVipPriceVO> listValidVipPrice(Long vipId) {
        return list(new LambdaQueryWrapper<ChatVipPriceVO>()
                .eq(ChatVipPriceVO::getValid, ValidEnum.VALID)
                .eq(ChatVipPriceVO::getVipId, vipId)
        .orderByAsc(ChatVipPriceVO::getSort));
    }
}
