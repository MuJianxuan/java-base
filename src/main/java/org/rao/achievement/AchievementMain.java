package org.rao.achievement;

import org.rao.achievement.impl.TestAchievement;
import org.rao.redis.RedissonDistributedLock;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;

import java.util.Map;

/**
 * @author Rao
 * @Date 2021/11/24
 **/
public class AchievementMain {

    public static void main(String[] args) throws Exception {

//
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6379").setDatabase(0).setPassword("123456");
//        config.setCodec(StringCodec.INSTANCE);
//        RedissonClient redissonClient = Redisson.create(config);
//        RedissonDistributedLock.redissonClient = redissonClient;
//
//        TestAchievement testAchievement = new TestAchievement(redissonClient);
//        testAchievement.afterPropertiesSet();
//
//        testAchievement.addAchievementValue( "rao",225800L - 41190L);
//        testAchievement.addAchievementValue( "rao",91190L);






    }

}
