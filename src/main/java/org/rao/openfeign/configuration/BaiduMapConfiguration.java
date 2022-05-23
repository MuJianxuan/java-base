package org.rao.openfeign.configuration;

import feign.codec.Decoder;
import org.rao.openfeign.fallbackfactory.BaiduMapFeignClientFallbackFactory;
import org.rao.openfeign.properties.BaiduMapProperties;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@Configuration
@ComponentScan("org.rao.openfeign")
@EnableFeignClients(basePackages = {"org.rao.openfeign.api"})
@Import({BaiduMapFeignClientFallbackFactory.class, BaiduMapProperties.class})
public class BaiduMapConfiguration {

    /**
     * 添加类型解析
     * @return
     */
    @Bean
    public Decoder feignDecoder() {
        BaiduMappingJackson2HttpMessageConverter converter = new BaiduMappingJackson2HttpMessageConverter();
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters( converter);
        return new SpringDecoder(objectFactory);
    }

    public static class BaiduMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
        public BaiduMappingJackson2HttpMessageConverter() {

            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add( MediaType.TEXT_PLAIN);
            mediaTypes.add( new MediaType("text","javascript"));
            this.setSupportedMediaTypes( mediaTypes );
        }
    }

}
