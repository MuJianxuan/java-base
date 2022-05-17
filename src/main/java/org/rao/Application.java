package org.rao;

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

    @Override
    public void run(String... args) throws Exception {


    }
}
