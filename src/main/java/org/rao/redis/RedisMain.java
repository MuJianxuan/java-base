package org.rao.redis;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

/**
 * @author Rao
 * @Date 2021/11/16
 **/
@Slf4j
public class RedisMain {

    public static void main(String[] args) {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(0).setPassword("123456");
        config.setCodec(StringCodec.INSTANCE);
        RedissonClient redissonClient = Redisson.create(config);

        RMap<String, Integer> testMap = redissonClient.getMap("map",StringCodec.INSTANCE);

        System.out.println( testMap.addAndGet("1",10));
        System.out.println(testMap.addAndGet("1", -5));


    }

}
