package org.rao.hutool;

import cn.hutool.core.lang.WeightRandom;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rao
 * @Date 2021/11/12
 **/
public class HutoolMain {

    public static void main(String[] args) {


//        weightObjList.add( new WeightRandom.WeightObj<>("3",3));
        List<WeightRandom.WeightObj<String>> weightObjList = new ArrayList<>();
        weightObjList.add( new WeightRandom.WeightObj<>("1",0));
        weightObjList.add( new WeightRandom.WeightObj<>("2",2));
        WeightRandom<String> random = RandomUtil.weightRandom(weightObjList);





        // 带权重的概率抽奖策略

        // 排队优先抽奖策略

        // 分段式礼物池抽奖策略





        long start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {


//            List<WeightRandom.WeightObj<String>> weightObjList = new ArrayList<>();
//            weightObjList.add( new WeightRandom.WeightObj<>("1",1));
//            weightObjList.add( new WeightRandom.WeightObj<>("2",2));
//            WeightRandom<String> random = RandomUtil.weightRandom(weightObjList);
//            String next = random.next();
            String next = random.next();
            if( "1".equals(next))
            System.out.println(JSON.toJSONString( next));

        }

        long end = System.currentTimeMillis();
        System.out.println("mmm:" + (end-start) );


    }

}
