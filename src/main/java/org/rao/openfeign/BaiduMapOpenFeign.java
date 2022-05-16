package org.rao.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/16
 **/
@FeignClient(name = "baiduMapOpenFeign",url = "https://api.map.baidu.com",configuration = {})
public interface BaiduMapOpenFeign {

    /**
     * 查询地址经纬度信息
     * @param address 地址
     * @param output json
     * @param ak 密钥
     * @return
     */
    @GetMapping("geocoding/v3")
    String queryAddressLongitudeAndLatitudeInformation(@RequestParam("address") String address, @RequestParam("output") String output, @RequestParam("ak") String ak);

}
