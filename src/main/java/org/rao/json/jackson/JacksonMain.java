package org.rao.json.jackson;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.rao.base.A;

import java.math.BigDecimal;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/03/28
 **/
@Slf4j
public class JacksonMain {
    public static void main(String[] args) throws JsonProcessingException {


        BigDecimal bigDecimal1 = new BigDecimal("1");
        BigDecimal bigDecimal2 = new BigDecimal("2");
        BigDecimal add = bigDecimal1.add(bigDecimal2);

        System.out.println( bigDecimal1.compareTo( bigDecimal2 ));
        System.out.println( bigDecimal1);
        System.out.println(bigDecimal2);
        System.out.println(add);


//        ObjectMapper objectMapper = new ObjectMapper();
//        // 需要自定义
////        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
//
//        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
//
//        A a = new A();
////        A a = new A("111");
////        HashMap<Object, Object> a = new HashMap<>();
//
//        log.info( "fastjson:{}" ,JSON.toJSONString( a ));
//
//        log.info("jackson:{}", objectMapper.writeValueAsString( a ) );


    }
}
