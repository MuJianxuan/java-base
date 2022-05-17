package org.rao.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/17
 **/
@FeignClient(name = "baiduTestFeignClient",url = "${spring.yum:https://api.map.baidu.com}")
public interface BaiduTestFeignClient {

    /**
     * 查询地址经纬度信息
     * @param address 地址
     * @param output json
     * @param ak 密钥
     * @return
     */
    @GetMapping("geocoding/v3")
    String queryAddressLongitudeAndLatitudeInformation(@RequestParam("address") String address);

}
