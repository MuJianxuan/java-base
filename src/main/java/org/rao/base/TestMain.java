package org.rao.base;

import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rao
 * @Date 2021/11/20
 **/
public class TestMain {
    public static void main(String[] args) {
        List<A> list = Arrays.asList(new A(), new A());

        List<Ai> collect = list.stream().map(a -> (Ai) a).collect(Collectors.toList());
        System.out.println(JSON.toJSONString( collect));

    }
}
