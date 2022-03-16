package org.rao.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  某公司的雪花算法，某开发分享，我Copy的。
 *     某开发回复道： 目前使用一切ok
 * @author Rao
 * @Date 2021/11/08
 **/
public class Snowflake {
    private static final Logger logger = LoggerFactory.getLogger(Snowflake.class);
    private static final long START_STMP = 1551677477786L;
    private static final long SEQUENCE_BIT = 12L;
    private static final long MACHINE_BIT = 5L;
    private static final long DATA_CENTER_BIT = 5L;
    private static final long MAX_DATA_CENTER_NUM = 31L;
    private static final long MAX_MACHINE_NUM = 31L;
    private static final long MAX_SEQUENCE = 4095L;
    private static final long MACHINE_LEFT = 12L;
    private static final long DATA_CENTER_LEFT = 17L;
    private static final long TIMESTMP_LEFT = 22L;
    private long dataCenterId;
    private long machineId;
    private long sequence = 0L;
    private long startStmp;
    private long lastStmp = -1L;

    public Snowflake(long dataCenterId, long machineId, long startStmp) {
        this.checkDataCenterId(dataCenterId);
        this.checkMachineId(machineId);
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
        this.startStmp = startStmp;
    }

    private void checkDataCenterId(long dataCenterId) {
        if (dataCenterId > 31L || dataCenterId < 0L) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATA_CENTER_NUM or less than 0");
        }
    }

    private void checkMachineId(long machineId) {
        if (machineId > 31L || machineId < 0L) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
    }

    public synchronized long nextId() {
        long currStmp = this.getNewStmp();
        if (currStmp < this.lastStmp) {
            long offset = this.lastStmp - currStmp;
            if (offset > 5L) {
                throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastStmp - currStmp));
            }

            try {
                logger.warn("waiting for clock to get current timeStamp again");
                this.wait(offset << 1);
                currStmp = this.getNewStmp();
                if (currStmp < this.lastStmp) {
                    throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", this.lastStmp - currStmp));
                }
            } catch (InterruptedException var6) {
                throw new RuntimeException( "do get nextId error,msg:" +  var6.getMessage() );
            }
        }

        if (currStmp == this.lastStmp) {
            this.sequence = this.sequence + 1L & 4095L;
            if (this.sequence == 0L) {
                currStmp = this.getNextMill();
            }
        } else {
            this.sequence = 0L;
        }

        this.lastStmp = currStmp;
        return currStmp - this.startStmp << 22 | this.dataCenterId << 17 | this.machineId << 12 | this.sequence;
    }

    private long getNextMill() {
        long mill;
        for(mill = this.getNewStmp(); mill <= this.lastStmp; mill = this.getNewStmp()) {
        }

        return mill;
    }

    private long getNewStmp() {
        return System.currentTimeMillis();
    }
}