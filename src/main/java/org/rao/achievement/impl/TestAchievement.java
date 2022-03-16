package org.rao.achievement.impl;

import lombok.extern.slf4j.Slf4j;
import org.rao.achievement.AbstractAchievementManager;
import org.rao.achievement.stage.AchievementStage;
import org.redisson.api.RedissonClient;

import java.util.Arrays;
import java.util.List;

/**
 * @author Rao
 * @Date 2021/11/24
 **/
@Slf4j
public class TestAchievement extends AbstractAchievementManager {
    public TestAchievement(RedissonClient redissonClient) {
        super(redissonClient);
    }

    @Override
    protected String getAchievementServiceName() {
        return "1";
    }

    @Override
    public void achievementTrigger(String userId, Long achievementValue) {


        log.debug("achievementTrigger userId:{} ,achievementValue:{},{}",userId,achievementValue,super.getBeInStage( userId));
    }

    @Override
    protected AchievementStage initAchievementStage() {
        List<Long> data = Arrays.asList(20000L, 50000L, 100000L, 200000L);
        return new AchievementStage(data);
    }
}
