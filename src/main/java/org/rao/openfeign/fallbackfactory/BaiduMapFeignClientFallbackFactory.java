package org.rao.openfeign.fallbackfactory;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.rao.openfeign.api.BaiduMapFeignClient;
import org.rao.openfeign.model.dto.BaiduMapGeoCodingDto;
import org.rao.openfeign.model.dto.BaiduMapRegionDto;
import org.rao.openfeign.model.vo.BaiduMapGeoCodingResult;
import org.rao.openfeign.model.vo.BaiduMapRegionResult;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@Slf4j
public class BaiduMapFeignClientFallbackFactory implements FallbackFactory<BaiduMapFeignClient> {
    @Override
    public BaiduMapFeignClient create(Throwable throwable) {
        log.error("[BaiduMapFeignClientFallbackFactory] 请求百度地图api接口错误！",throwable);
        return new BaiduMapFeignClient(){

            @Override
            public BaiduMapGeoCodingResult getGeoCoding(BaiduMapGeoCodingDto baiduMapGeoCodingDto) {
                return new BaiduMapGeoCodingResult();
            }

            @Override
            public BaiduMapRegionResult getRegion(BaiduMapRegionDto baiduMapRegionDto) {
                return new BaiduMapRegionResult();
            }
        };
    }
}
