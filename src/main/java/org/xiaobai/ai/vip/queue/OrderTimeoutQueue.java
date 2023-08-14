package org.xiaobai.ai.vip.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.xiaobai.ai.vip.service.ChatOrderService;
import org.xiaobai.core.enums.OrderStatusEnum;
import org.xiaobai.core.utils.IDistributedLockUtil;
import org.xiaobai.core.utils.RedisUtil;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
public class OrderTimeoutQueue {

    private final static String QUEUE_TIME_OUT_KEY = "order:timeout";

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private IDistributedLockUtil iDistributedLockUtil;
    @Resource
    private ChatOrderService chatOrderService;

    /**
     * 添加进延时队列
     * @param orderId
     */
    public void enqueueOrderTimeout(String orderId) {
        long timestamp = System.currentTimeMillis() + 1 * 60 * 1000;
        redisUtil.zAdd(QUEUE_TIME_OUT_KEY, orderId, timestamp);
    }

    /**
     *  移除某个延时队列value
     * @param orderId
     */
    public void dequeueOrderTimeout(String orderId) {
        redisUtil.zRemove(QUEUE_TIME_OUT_KEY, orderId);
    }

    public void processTimeoutOrders() throws InterruptedException {
        iDistributedLockUtil.lockReturnAndDo("orders", (key -> {
            long currentTimestamp = System.currentTimeMillis();
            Set<ZSetOperations.TypedTuple<String>> timeoutOrders = redisUtil.zRangeByScoreWithScores(QUEUE_TIME_OUT_KEY, 0, currentTimestamp);
            if (CollectionUtils.isEmpty(timeoutOrders)) {
                return;
            }
            for (ZSetOperations.TypedTuple<String> tuple : timeoutOrders) {
                Double score = tuple.getScore();
                String orderId = tuple.getValue();
                if (Objects.isNull(score)) {
                    continue;
                }
                if (score <= currentTimestamp) {
                    // 处理超时逻辑，更新订单状态为超时状态
                    if (!updateOrderStatus(orderId, OrderStatusEnum.TIME_OUT)) {
                       log.warn("更新订单状态失败：orderId: {}", orderId);
                    }
                }
            }
            // 清理已处理的超时订单
            redisUtil.zRemoveRangeByScore("order:timeout", 0, currentTimestamp);
        }));
    }

    private Boolean updateOrderStatus(String orderId, OrderStatusEnum status) {
        return chatOrderService.updateStatus(Long.valueOf(orderId), status);
    }
}
