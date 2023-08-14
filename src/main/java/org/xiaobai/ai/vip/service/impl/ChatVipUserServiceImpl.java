package org.xiaobai.ai.vip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.xiaobai.ai.vip.entity.ChatOrderVO;
import org.xiaobai.ai.vip.entity.ChatVipPriceVO;
import org.xiaobai.ai.vip.entity.ChatVipUserVO;
import org.xiaobai.ai.vip.mapper.ChatVipUserMapper;
import org.xiaobai.ai.vip.service.ChatVipPriceService;
import org.xiaobai.ai.vip.service.ChatVipService;
import org.xiaobai.ai.vip.service.ChatVipUserService;
import org.xiaobai.core.utils.DateTimeUtil;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName ChatVipUserServiceImpl
 * @Author dingfeng.xiao
 * @Date 2023/7/10 10:07
 * @Version 1.0
 */
@Slf4j
@Service
public class ChatVipUserServiceImpl extends ServiceImpl<ChatVipUserMapper, ChatVipUserVO> implements ChatVipUserService {

    @Resource
    private ChatVipPriceService chatVipPriceService;
    @Lazy
    @Resource
    private ChatVipService chatVipService;

    @Override
    public Boolean addUserVip(ChatOrderVO orderVO) {
        ChatVipUserVO chatVipUserVO = new ChatVipUserVO();
        Long priceId = orderVO.getVipPriceId();
        ChatVipPriceVO vipPriceVO = chatVipPriceService.getById(priceId);
        if (Objects.isNull(vipPriceVO)) {
            log.error("会员价格信息有误：order：{}", orderVO);
            return false;
        }
        ChatVipUserVO vipUserVO = getOne(new LambdaQueryWrapper<ChatVipUserVO>().eq(ChatVipUserVO::getUserId, orderVO.getUserId())
                .eq(ChatVipUserVO::getVipId, vipPriceVO.getVipId()));
        if (Objects.nonNull(vipUserVO)) {
            // 续期
            Date nextTime = DateTimeUtil.nextTime(vipUserVO.getEndTime(), vipPriceVO.getDateCount(), vipPriceVO.getDateType());
            vipUserVO.setEndTime(nextTime);
            return updateById(vipUserVO);
        } else {
            createVipUserVO(orderVO, chatVipUserVO, vipPriceVO);
            return save(chatVipUserVO);
        }
    }

    private void createVipUserVO(ChatOrderVO orderVO, ChatVipUserVO chatVipUserVO, ChatVipPriceVO vipPriceVO) {
        Date endDate = DateTimeUtil.nextTime(vipPriceVO.getDateCount(), vipPriceVO.getDateType());
        chatVipUserVO.setVipId(vipPriceVO.getVipId());
        chatVipUserVO.setStartTime(new Date());
        chatVipUserVO.setEndTime(endDate);
        chatVipUserVO.setUserId(orderVO.getUserId());
    }

    @Override
    public List<ChatVipUserVO> getUserVip(Long userId) {
        return list(new LambdaQueryWrapper<ChatVipUserVO>()
        .eq(ChatVipUserVO::getUserId, userId)
        .ge(ChatVipUserVO::getEndTime, new Date())).stream().peek(v -> {
            v.setVipInfo(chatVipService.getById(v.getVipId()));
        }).collect(Collectors.toList());
    }
}
