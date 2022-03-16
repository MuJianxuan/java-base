package org.rao.list;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Rao
 * @Date 2021/11/12
 **/
public class ListMain {

    public static void main(String[] args) {

        String[] arr = new String[]{"1","2","3"};

        Stream.of( arr ).forEach(System.out::println);

//        List<Integer> list = new ArrayList<>();
//        list.add(1);
//        list.add(2);
//        list.add(3);
//        list.add(4);
//
//
//        list.sort( (o1,o2) -> {
////            return o1 < o2 ? -1 : 0;
//            return 0;
//        } );
//
//        System.out.println(JSON.toJSONString(list));
//

    }

}
