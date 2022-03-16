package org.rao.achievement.stage;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import lombok.Data;

import java.util.*;

/**
 * 成就阶段
 * @author Rao
 * @Date 2021/11/18
 **/
@Data
public class AchievementStage {

    /**
     * 固定key
     */
    private final static String FIXED_KEY = "fixed";

    /**
     * 第一阶段值
     */
    private final static String FIRST_STAGE_KEY = "first_stage";

    /**
     * 固定值 : xN
     *
     * 非固定值  10,30,50
     *     first_stage -> 10  初始阶段
     *     1     ->    30    第一阶段
     *     2    ->    50    第二阶段
     */
    private Map<String,Long> achievementStageMap = new HashMap<>();
    /**
     * 是否等值
     */
    private Boolean equivalent;


    /**
     * 固值
     * @param equivalentValue 每一阶段值
     */
    public AchievementStage(Long equivalentValue) {
        this.equivalent = true;
        Assert.notNull( equivalentValue);
        this.achievementStageMap.put( FIRST_STAGE_KEY, equivalentValue);
        this.achievementStageMap.put( FIXED_KEY, equivalentValue);
    }

    /**
     * 非固值
     * @param achievementStageValueList 固定成就阶段值列表
     */
    public AchievementStage(List<Long> achievementStageValueList ){
        Assert.isTrue(CollUtil.isNotEmpty( achievementStageValueList));
        this.equivalent = false;
        // 从小到大排列  o1 >= o2  为 1 表顺序  o1 >= o1 表倒序
        achievementStageValueList.sort( (o1,o2) -> o1 >= o2 ? 0 : -1);

        // 第一阶段值
        this.achievementStageMap.put( FIRST_STAGE_KEY,achievementStageValueList.get( 0));
        for (int i = 1; i < achievementStageValueList.size(); i++) {
            // 阶段设置  10,30,50
            // f --> 10
            // 1 --> 30
            // 2 --> 50
            this.achievementStageMap.put( i+"" ,achievementStageValueList.get(i));
        }
    }

    /**
     * 获取下一阶段成就值
     * @param currentInStageValue  当前所处阶段值
     * @return  is null，表示没有下一阶段了
     */
    public Long nextAchievementStageValue(Integer currentInStageValue){
        if(  currentInStageValue == 0 ){
            return this.achievementStageMap.get( FIRST_STAGE_KEY);
        }
        return equivalent ? currentInStageValue * this.achievementStageMap.get( FIXED_KEY): this.achievementStageMap.get( currentInStageValue+"");

    }

    public static void main(String[] args) {

        AchievementStage achievementStage = new AchievementStage(Arrays.asList(10L, 20L, 30L));
//        Long aLong = achievementStage.nextAchievementStageValue(4 );
//        System.out.println(aLong);
//
//        Map<String, Long> achievementStageMap = achievementStage.getAchievementStageMap();
////        achievementStageMap.put( FIXED_KEY,1L);
//
//        Integer stage = 3;
//
//        Map<String,Long> achievementStageMap = new HashMap<>();
//        achievementStageMap.put(FIXED_KEY,null);
//        achievementStageMap.put("1",10L);
//        achievementStageMap.put("2",30L);
//        achievementStageMap.put("3",50L);3
        int i =10000;
        System.out.println(achievementStage.nextAchievementStageValue(999
        ));
        // 空指针
//        Long c =  false ? stage * achievementStageMap.get( FIXED_KEY) : achievementStageMap.get( stage+"");

//        Long nextAchievementStageValue = Optional.ofNullable( false ? stage+1 * achievementStageMap.get( FIXED_KEY) : achievementStageMap.get( (stage +1 )+"") ).orElse(0L) - (  false ? stage * achievementStageMap.get( FIXED_KEY) : achievementStageMap.get( stage+"") );
//
//
//        System.out.println( nextAchievementStageValue);




        // 新的阶段的要达成的差值  下一阶段减去当前阶段的值  if  负数 则表示 没有后续阶段了
//        Long nextAchievementStageValue = Optional.ofNullable( achievementStage.nextAchievementStageValue( 3) ).orElse(0L) - achievementStage.nextAchievementStageValue( 2);
//        System.out.println( nextAchievementStageValue);


    }

}
