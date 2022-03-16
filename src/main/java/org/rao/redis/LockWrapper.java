package org.rao.redis;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author Rao
 * @Date 2021/11/16
 **/
@Setter
@Getter
@Accessors(chain = true)
public class LockWrapper {

    private String key;
    /**
     * 等待获取锁的时间
     */
    private long waitTime;
    /**
     * 拥有锁的时间
     */
    private long leaseTime = -1;
    /**
     * 单位
     */
    private TimeUnit unit;


}
