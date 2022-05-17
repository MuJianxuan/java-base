package org.rao.openfeign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.rao.openfeign.configuration.BaiduMapProperties;

import javax.annotation.Resource;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@Slf4j
public class BaiduMapRequestInterceptor implements RequestInterceptor {

    @Resource
    private BaiduMapProperties baiduMapProperties;

    @Override
    public void apply(RequestTemplate template) {
        template.query("output","xml");
        template.query("ak",baiduMapProperties.getAk() );
    }
}
