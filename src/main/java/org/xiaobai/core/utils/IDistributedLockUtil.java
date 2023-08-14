package org.xiaobai.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.xiaobai.core.exception.TipException;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Service
@Slf4j
public class IDistributedLockUtil {
    /**
     * 统一前缀
     */
    private static final String PREFIX = "DistributedLOCK";
    @Resource
    private RedissonClient redissonClient;

    /**
     * 有锁直接返回，获取到锁执行方法
     *
     * @param key
     * @return
     */
    public void lockReturnAndDo(String key, Consumer<String> consumer) {
        RLock lock = getLock(key, false);
        try {
            if (lock.isLocked()) {
                return;
            }
            if (lock.tryLock()) {
                consumer.accept(key);
            }
        } catch (Exception e) {
            log.error("lockReturnAndDo:" + key, e);
            throw new TipException(e.getMessage());
        } finally {
            this.unLock(lock);
        }
    }

    public RLock lock(String key) {
        return this.lock(key, 0L, TimeUnit.SECONDS, false);
    }

    public RLock lock(String key, long lockTime, TimeUnit unit, boolean fair) {
        RLock lock = getLock(key, fair);
        // 获取锁,失败一直等待,直到获取锁,不支持自动续期
        if (lockTime > 0L) {
            lock.lock(lockTime, unit);
        } else {
            // 具有Watch Dog 自动延期机制 默认续30s 每隔30/3=10 秒续到30s
            lock.lock();
        }
        return lock;
    }

    public RLock tryLock(String key, long tryTime) throws Exception {
        return this.tryLock(key, tryTime, 0L, TimeUnit.SECONDS, false);
    }

    public RLock tryLock(String key, long tryTime, long lockTime, TimeUnit unit, boolean fair)
            throws Exception {
        RLock lock = getLock(key, fair);
        boolean lockAcquired;
        // 尝试获取锁，获取不到超时异常,不支持自动续期
        if (lockTime > 0L) {
            lockAcquired = lock.tryLock(tryTime, lockTime, unit);
        } else {
            // 具有Watch Dog 自动延期机制 默认续30s 每隔30/3=10 秒续到30s
            lockAcquired = lock.tryLock(tryTime, unit);
        }
        if (lockAcquired) {
            return lock;
        }
        return null;
    }

    /**
     * 获取锁
     *
     * @param key
     * @param fair
     * @return
     */
    private RLock getLock(String key, boolean fair) {
        RLock lock;
        String lockKey = PREFIX + ":" + key;
        if (fair) {
            // 获取公平锁
            lock = redissonClient.getFairLock(lockKey);
        } else {
            // 获取普通锁
            lock = redissonClient.getLock(lockKey);
        }
        return lock;
    }

    public void unLock(RLock lock) {
        if (Objects.isNull(lock)) {
            return;
        }
        if (lock.isLocked()) {
            try {
                lock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.error("释放分布式锁异常", e);
            }
        }
    }

}
