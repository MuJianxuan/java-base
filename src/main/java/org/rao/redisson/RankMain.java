package org.rao.redisson;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.IntegerCodec;
import org.redisson.client.codec.LongCodec;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.ScoredEntry;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Rao
 * @Date 2021/11/18
 **/
@Slf4j
public class RankMain {

    public static void main(String[] args) {

        Config config = new Config();
//        config.useSingleServer().setAddress("redis://dev-db.meiqijiacheng.com:6379").setDatabase(0).setPassword("sango@2020");
        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(0).setPassword("123456");
        config.setCodec(StringCodec.INSTANCE);
        RedissonClient redissonClient = Redisson.create(config);

//        RMap<String, BigDecimal> clientMap = redissonClient.getMap("fff", JsonJacksonCodec.INSTANCE);
//        clientMap.putIfAbsent("rao",new BigDecimal("1000.5").setScale(1, RoundingMode.DOWN));
//
//        clientMap.addAndGet("rao",new BigDecimal("3.5"));
//
//        System.out.println( clientMap.get( "rao"));


//        RMap<String, Integer> ddd = redissonClient.getMap("ddd", JsonJacksonCodec.INSTANCE);
//        ddd.put("1",1);
//        ddd.put("2",2);
//        ddd.put("3",3);

//        Map<String, Integer> stringIntegerMap = ddd.readAllMap();

//        ddd.clear();
//        ddd.put("1",1 );
//
//        Map<String, Integer> stringIntegerMap = ddd.readAllMap();
//        System.out.println(JSON.toJSONString(stringIntegerMap));

//        System.out.println(ddd.containsKey("1"));

//        System.out.println(JSON.toJSONString(stringIntegerMap));

//        RMap<String, Integer> map = redissonClient.getMap("lottery:blackFridayLottery:619b746e2f1164c6cb7473ab", FastJsonCodec.INSTANCE);
//        Map<String, Integer> stringIntegerMap = map.readAllMap();


//        //测试
//        RankService rankService = new RankService(redissonClient);
//
//        String leaderboard = "leaderboard";
//
//        RMap<String, Long> map = redissonClient.getMap("hhh", LongCodec.INSTANCE);
//        Long aLong = map.get("1");
//        System.out.println(aLong);
//
//        Long put = map.put("1", 10L);
//        Long aLong2 = map.get("1");
//        System.out.println(aLong2);
//
//
//        rankService.refreshRank(leaderboard,"1",1);
//        rankService.refreshRank(leaderboard,"2",2);
//        rankService.refreshRank(leaderboard,"3",3);
//        rankService.refreshRank(leaderboard,"4",4);
//        rankService.refreshRank(leaderboard,"5",5);
//
//
//
//
//        // 返回倒排的数据
//        List<String> collection = (List<String>) rankService.list(leaderboard, 0, 50);
//        System.out.println(JSON.toJSONString( collection));
//
//        Map<String, Integer> stringIntegerMap = rankService.batchGetUserRank(leaderboard, Arrays.asList("1", "8", "9", "4"));
//        System.out.println(JSON.toJSONString( stringIntegerMap));

//
//
//        Collection<ScoredEntry<String>> page = rankService.page(leaderboard, 0, 50);
//        System.out.println(JSON.toJSONString( page));
//
//        List<Integer> integers = Arrays.asList(1, 3, 5, 6, 2, 5);
//
//        Collections.sort( collection);
//        System.out.println(JSON.toJSONString( collection));
//
//        Collections.sort( integers,(o1,o2) -> {
//            if(o1.equals(o2)) return 0;
//            // 1不变，
//            return  o1 > o2 ? -1 : 1;
//        } );
//        System.out.println(JSON.toJSONString( integers));

        System.exit(0);

    }

}
