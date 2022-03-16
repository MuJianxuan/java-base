package org.rao.netty.rpc;

import org.rao.netty.rpc.jdkproxy.RpcProxyHandler;
import org.rao.netty.rpc.ref.HelloService;

import java.lang.reflect.Proxy;

/**
 * RPC Client
 * @author Rao
 * @Date 2021/12/21
 **/
public class RpcClient {

    public static void main(String[] args) {


        // 代理解析
        HelloService helloService = (HelloService) Proxy.newProxyInstance( Thread.currentThread().getContextClassLoader() , new Class[]{HelloService.class}, new RpcProxyHandler( HelloService.class));

        long start = System.currentTimeMillis();
        String hello = helloService.hello("rpc ");
        System.out.println( hello);

        System.out.println( "ms" +  (System.currentTimeMillis() - start ) );

        helloService.hello("ex");


    }

}
