package org.rao.openfeign.api;

import org.rao.openfeign.fallbackfactory.BaiduMapFeignClientFallbackFactory;
import org.rao.openfeign.interceptor.BaiduMapRequestInterceptor;
import org.rao.openfeign.model.dto.BaiduMapGeoCodingDto;
import org.rao.openfeign.model.dto.BaiduMapRegionDto;
import org.rao.openfeign.model.vo.BaiduMapGeoCodingResult;
import org.rao.openfeign.model.vo.BaiduMapRegionResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * desc: 百度地图接口
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@FeignClient(name = "baiduMapFeignClient",url = "${application.baidu-map.url:https://api.map.baidu.com}",configuration = {BaiduMapRequestInterceptor.class},fallbackFactory = BaiduMapFeignClientFallbackFactory.class)
public interface BaiduMapFeignClient {

    /**
     * 获取地址经纬度信息
     * @return
     */
    @GetMapping(path = "/geocoding/v3/")
    BaiduMapGeoCodingResult getGeoCoding(@SpringQueryMap BaiduMapGeoCodingDto baiduMapGeoCodingDto );

    /**
     * 获取行政区划分查询
     */
    @GetMapping(path = "/api_region_search/v1/")
    BaiduMapRegionResult getRegion(@SpringQueryMap BaiduMapRegionDto baiduMapRegionDto);

}
