package org.rao.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBatch;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.ScoredEntry;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 榜单服务
 */
@Slf4j
public class RankService {

    private final RedissonClient redissonClient;

    public RankService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    /**
     * 刷新排名
     * @param rankKey 排行榜KEY
     * @param userId  用户
     * @param score   权重
     * @return 刷新后的排名，排名 从 1 开始
     */
    public Integer refreshRank(String rankKey, String userId, long score) {
        if(score <= 0){
           return 0;
        }
        return redissonClient.getScoredSortedSet(rankKey, StringCodec.INSTANCE).addScoreAndGetRevRank(userId,score) + 1;
    }

    /**
     * 获取用户排名
     *
     * @param rankKey 排行榜KEY
     * @param userId  用户
     * @return 排名 从 1 开始,0表示用户未参数排名
     */
    public Integer getRank(String rankKey, String userId) {
        Integer rank = redissonClient.getScoredSortedSet(rankKey, StringCodec.INSTANCE).revRank(userId);
        if (rank == null) {
            return 0;
        }
        return rank + 1;
    }

    /**
     * 获取用户当前分数
     * @param rankKey 排行榜KEY
     * @param userId 用户
     * @return 用户积分数
     */
    public double getScore(String rankKey, String userId){
        Double score = redissonClient.getScoredSortedSet(rankKey, StringCodec.INSTANCE).getScore(userId);
        if(score == null){
            return 0;
        }
        return score;
    }

    /**
     * 批量获取 用户排名
     * @param rankKey
     */
    public Map<String, Integer> batchGetUserRank(String rankKey, List<String> userIdColl){
        RBatch batch = redissonClient.createBatch();
        userIdColl.forEach(userId -> {
            batch.getScoredSortedSet(rankKey, StringCodec.INSTANCE).revRankAsync(userId);
        });
        List<?> responseResult = batch.execute().getResponses();

        if( responseResult.size() != userIdColl.size() ){
            return null;
        }
        Map<String,Integer> userIdRankMap = new HashMap<>();

        for (int i = 0; i < userIdColl.size(); i++) {
            userIdRankMap.put( userIdColl.get(i), (Integer) responseResult.get(i));
        }

        return userIdRankMap;

    }

    /**
     * 分页获取排行榜
     *
     * @param rankKey 排行榜KEY
     * @param page    页码: 从 1 开始
     * @param size    数量
     * @return users
     */
    public Collection<ScoredEntry<String>> page(String rankKey, int page, int size) {
        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(rankKey, StringCodec.INSTANCE);
        return scoredSortedSet.entryRangeReversed((page - 1) * size, page * size - 1);
    }

    /**
     * 获取某个范围的数据
     *
     * @param rankKey    排行榜KEY
     * @param startIndex 起始索引: 从 0 开始
     * @param endIndex   结束索引 包含
     * @return users
     */
    public Collection<String> list(String rankKey, int startIndex, int endIndex) {
        RScoredSortedSet<String> scoredSortedSet = redissonClient.getScoredSortedSet(rankKey, StringCodec.INSTANCE);
        return scoredSortedSet.valueRangeReversed(startIndex, endIndex);
    }
}
