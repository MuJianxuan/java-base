package org.rao.openfeign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.rao.openfeign.constant.BaiduMapConstant;
import org.rao.openfeign.properties.BaiduMapProperties;
import org.springframework.util.StringUtils;

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

        template.query(BaiduMapConstant.AK_KEY, baiduMapProperties.getAk());
        if( StringUtils.isEmpty( baiduMapProperties.getAk() ) ){
            log.warn("[百度地图查询接口] ak参数未填写，请求会失败！");
        }
    }
}
