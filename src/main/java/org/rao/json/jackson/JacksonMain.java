package org.rao.json.jackson;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.rao.base.A;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/03/28
 **/
@Slf4j
public class JacksonMain {
    public static void main(String[] args) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        // 需要自定义

        A a = new A();
//        HashMap<Object, Object> a = new HashMap<>();

        log.info( "fastjson:{}" ,JSON.toJSONString( a ));

        log.info("jackson:{}", objectMapper.writeValueAsString( a ) );


    }
}
