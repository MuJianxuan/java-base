package org.rao.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.function.Supplier;

/**
 * 基于 Redisson 实现的
 *   尝试使用！
 * @author Rao
 * @Date 2021/11/16
 **/
@Slf4j
public class RedissonDistributedLock {

    /**
     * 需要初始化
     */
    public static RedissonClient redissonClient;

    /**
     * 获取锁后立即返回true 。 如果该锁当前被这个或分布式系统中的
     * 任何其他进程中的另一个线程持有，则此方法会在放弃并返回false
     * 之前一直尝试获取锁直到waitTime 。 如果获得了锁，
     * 它将一直保持到调用unlock为止，或者直到授予锁后的leaseTime已过 -
     * 以先到者为准。
     * @param lockWrapper
     * @param successLockSupplier
     * @param <T>
     * @return
     */
    public static <T> T tryLock(LockWrapper lockWrapper, Supplier<T> successLockSupplier,Supplier<T> failLockSupplier,VoidHandle voidHandle){

        // 没拿到锁的线程 调用 unlock 会抛出异常
        RLock lock = redissonClient.getLock(lockWrapper.getKey());

        try {
            /**
             * 源码解析：  该设置无 watch dog 关注，因此会导致某些问题
             * 1、没有看 waiting 的实现，要么阻塞，要么循环等待
             * 2、关于 释放的实现是 当没有设置 leaseTime 时，看门狗才会生效；
             * 3、默认实现原理是：利用定时任务，总共续30s,则每 30 /3的s 下会续一次。
             */
            if (lock.tryLock( lockWrapper.getWaitTime(), lockWrapper.getLeaseTime(), lockWrapper.getUnit())) {
                try {
                    return successLockSupplier.get();
                } finally {
                    try {
                        lock.unlock();
                    }catch (Exception ex){
                        // 这里不回滚的话，则需要抛异常！
                        voidHandle.voidHandle();
                    }
                }
            }

        } catch (InterruptedException e) {
            log.info("");
        }
        return failLockSupplier.get();

    }

}
