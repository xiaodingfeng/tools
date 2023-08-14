package org.xiaobai.ai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.xiaobai.ai.entity.ChatUserBalanceVO;
import org.xiaobai.ai.mapper.ChatUserBalanceMapper;
import org.xiaobai.ai.response.DayBalanceResponse;
import org.xiaobai.ai.service.ChatConfigService;
import org.xiaobai.ai.service.ChatUserBalanceService;
import org.xiaobai.ai.vip.entity.ChatFreeConfigVO;
import org.xiaobai.ai.vip.entity.ChatVipUserVO;
import org.xiaobai.ai.vip.entity.ChatVipVO;
import org.xiaobai.ai.vip.service.ChatFreeConfigService;
import org.xiaobai.ai.vip.service.ChatVipUserService;
import org.xiaobai.core.exception.TipException;
import org.xiaobai.core.utils.DateTimeUtil;
import org.xiaobai.core.utils.IDistributedLockUtil;
import org.xiaobai.core.utils.RedisUtil;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChatUserBalanceServiceImpl extends ServiceImpl<ChatUserBalanceMapper, ChatUserBalanceVO> implements ChatUserBalanceService {

    /**
     * 领取额度锁key
     */
    private static final String RECEIVE_BALANCE_PREFIX = "user_balance_receive:";
    @Resource
    private ChatVipUserService chatVipUserService;
    @Resource
    private ChatFreeConfigService chatFreeConfigService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IDistributedLockUtil iDistributedLockUtil;
    @Resource
    private ChatConfigService chatConfigService;

    /**
     * 用户额度
     *
     * @return
     */
    @Override
    public ChatUserBalanceVO getBalance(Long userId) {
        return getOne(new LambdaQueryWrapper<ChatUserBalanceVO>()
                .eq(ChatUserBalanceVO::getUserId, userId));
    }

    @Override
    public DayBalanceResponse dayBalance(Long userId) {
        DayBalanceResponse dayBalanceResponse = new DayBalanceResponse();

        // 先获取 是否已经领取
        String key = RECEIVE_BALANCE_PREFIX + DateTimeUtil.getCurrentDate();
        if (redisUtil.sIsMember(key, String.valueOf(userId))) {
            dayBalanceResponse.setCanBeReceive(false);
        } else {
            dayBalanceResponse.setCanBeReceive(true);
        }
        List<ChatVipUserVO> userVip = chatVipUserService.getUserVip(userId);
        if (CollectionUtils.isEmpty(userVip)) {
            ChatFreeConfigVO validConfig = chatFreeConfigService.findValidConfig();
            dayBalanceResponse.setBalance(Objects.isNull(validConfig) ? 0 : validConfig.getReceiveBalance());
            return dayBalanceResponse;
        }
        // 取vip等级最高的
         List<ChatVipVO> collect = userVip.stream().map(ChatVipUserVO::getVipInfo)
                 .sorted(Comparator.comparing(ChatVipVO::getSort).reversed()).collect(Collectors.toList());
        ChatVipVO chatVipVO = collect.get(0);
        dayBalanceResponse.setBalance(chatVipVO.getOptions().getReceiveBalance());
        return dayBalanceResponse;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean receiveBalance(Long userId) {
        String user = String.valueOf(userId);
        RLock lock = iDistributedLockUtil.lock("balance_receive:" + user);
        try {
            DayBalanceResponse dayBalance = dayBalance(userId);
            if (!dayBalance.getCanBeReceive()) {
                throw new TipException("今天已经领取过了！");
            }
            String key = RECEIVE_BALANCE_PREFIX + DateTimeUtil.getCurrentDate();
            if (increaseChatBalance(userId, dayBalance.getBalance())) {
                if (redisUtil.sAdd(key, user) == 1) {
                    return true;
                }
                throw new TipException("领取失败");
            }
            throw new TipException("领取失败");
        } finally {
            if (Objects.nonNull(lock)) {
                lock.unlock();
            }
        }
    }

    @Override
    public Boolean reduceChatBalance(Long userId, Integer balance) {
        return baseMapper.reduceChatBalance(userId, balance) > 0;
    }

    @Override
    public Boolean increaseChatBalance(Long userId, Integer balance) {
        return baseMapper.increaseChatBalance(userId, balance) > 0;
    }

    @Override
    public Boolean autoReduceChatBalance(Long userId) {
        int balance = chatConfigService.findChatBalance();
        return reduceChatBalance(userId, balance);
    }

    @Override
    public Boolean initUserBalance(Long userId) {
        if (getOne(new LambdaQueryWrapper<ChatUserBalanceVO>()
                .eq(ChatUserBalanceVO::getUserId, userId)) != null) {
            throw new TipException("用户已经完成初始化额度");
        }
        ChatUserBalanceVO chatUserBalanceVO = new ChatUserBalanceVO();
        chatUserBalanceVO.setChatBalance(999);
        chatUserBalanceVO.setUserId(userId);
        return save(chatUserBalanceVO);
    }
}
