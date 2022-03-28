package org.rao.netty.rpc;

import cn.hutool.core.thread.ConcurrencyTester;
import lombok.extern.slf4j.Slf4j;
import org.rao.netty.rpc.jdkproxy.RpcProxyHandler;
import org.rao.netty.rpc.ref.HelloService;

import java.lang.reflect.Proxy;

/**
 * RPC Client
 * @author Rao
 * @Date 2021/12/21
 **/
@Slf4j
public class RpcClient {

    public static void main(String[] args) {

        // 添加移除
        Runtime.getRuntime().addShutdownHook( new Thread(BootstrapClientUtils::destroyChannels));

        ConcurrencyTester concurrencyTester = new ConcurrencyTester(100);
        // 代理解析
        HelloService helloService = (HelloService) Proxy.newProxyInstance( Thread.currentThread().getContextClassLoader() , new Class[]{HelloService.class}, new RpcProxyHandler( HelloService.class));
        concurrencyTester.test( () -> {
            String hello = helloService.hello("rpc ");
        });
        log.info("耗时：" + concurrencyTester.getInterval() );

        System.exit(0);

    }

}
