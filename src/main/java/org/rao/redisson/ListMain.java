package org.rao.redisson;

import com.alibaba.fastjson.JSON;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.LongCodec;
import org.redisson.client.codec.StringCodec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import java.util.Map;

/**
 * @author Rao
 * @Date 2021/11/26
 **/
public class ListMain {

    public static void main(String[] args) {

        Config config = new Config();
//        config.useSingleServer().setAddress("redis://dev-db.meiqijiacheng.com:6379").setDatabase(0).setPassword("sango@2020");
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(0).setPassword("123456");
//        config.useSingleServer().setAddress("redis://redis-frankfurt.redis.germany.rds.aliyuncs.com:6379").setDatabase(0).setPassword("read_only:sango@2020");
//        config.setCodec(StringCodec.INSTANCE);
        RedissonClient redissonClient = Redisson.create(config);

        //61a7209c2f1164191aed70b8 林
        // 6162b2352f1164dc70519d65 我
//        redissonClient.getMap("TACTIVITY:new_friends:user_lottery_count_map",LongCodec.INSTANCE).addAndGet("61a7209c2f1164191aed70b8",100);

//        RMap<String, Long> aaa = redissonClient.getMap("aaa", LongCodec.INSTANCE);
//        Map<String, Long> stringLongMap = aaa.readAllMap();
//        System.out.println( JSON.toJSONString( stringLongMap) ) ;
//        Long a = aaa.addAndGet("a", 1);

        // 送礼成就Map
//        RMap<String, Long> sendGiftAchievementMap = redissonClient.getMap("achievement:value_bucket:BlackFridaySendGiftAchievement", JsonJacksonCodec.INSTANCE);

//        RMap<String, Long> asd = redissonClient.getMap("asd",new FastJsonCodec() );
//        asd.put("sdsd", 1L);
//
//        Map<String, Long> stringLongMap = asd.readAllMap();
//        System.out.println(JSON.toJSONString( stringLongMap ));

//        System.out.println(asd.get("sdsd"));


        System.exit(0);

    }
}
