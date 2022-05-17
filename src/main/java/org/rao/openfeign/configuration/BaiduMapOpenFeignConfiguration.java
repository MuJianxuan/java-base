package org.rao.openfeign.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * desc: 全局
 *
 * @author Rao
 * @Date 2022/05/16
 **/
@Configuration
@Import(FeignClientsConfiguration.class)
public class BaiduMapOpenFeignConfiguration {

    // 一个域名

    // 一个 ak


//    /**
//     * 这样配置所有的feignClient都会被拦截.
//     * @return
//     */
//    @Bean
//    public BaiduMapRequestInterceptor baiduMapRequestInterceptor(){
//        return new BaiduMapRequestInterceptor();
//    }


    /**
     * 拦截器
     */
    @Slf4j
    public static class BaiduMapRequestInterceptor implements RequestInterceptor {

        @Override
        public void apply(RequestTemplate template) {
            template.query("output","json");
            template.query("ak","ZDPtk1UBQ8hSvSHqVF31e7EoMBg9V7F2");
        }
    }



}

