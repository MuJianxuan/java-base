package org.rao;

import org.checkerframework.checker.units.qual.A;
import org.rao.openfeign.BaiduMapOpenFeign;
import org.rao.openfeign.BaiduTestFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * desc:
 *
 * @author Rao
 * @Date 2022/05/16
 **/
@SpringBootApplication
@EnableFeignClients
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

    @Autowired
    private BaiduMapOpenFeign baiduMapOpenFeign;
    @Autowired
    private BaiduTestFeignClient baiduTestFeignClient;

    @Override
    public void run(String... args) throws Exception {

//        String s = baiduTestFeignClient.queryAddressLongitudeAndLatitudeInformation("xx");
        String s = baiduMapOpenFeign.queryAddressLongitudeAndLatitudeInformation("水口");
        System.out.println(s);
    }
}
