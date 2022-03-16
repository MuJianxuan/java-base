package org.rao.netty;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

/**
 * @author Rao
 * @Date 2021/11/04
 **/
public class StudyFuture {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

        // 返回的是 netty 包下的 Future
        Future<Integer> future = nioEventLoopGroup.submit(() -> {
            Thread.sleep(1000);
            return 88;
        });

        // 增强了 java原生 Future 功能

        // 此外 netty 还提供了一个 承诺结果类
        EventLoop eventLoop = nioEventLoopGroup.next();
        DefaultPromise<Integer> defaultPromise = new DefaultPromise<>(eventLoop);

        nioEventLoopGroup.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            defaultPromise.setSuccess(88);
        });

        // get 方法 是阻塞的
        // getNow 是马上获取  此时有可能获取为null
        System.out.println(defaultPromise.getNow());
        System.out.println( defaultPromise.get() );
        nioEventLoopGroup.shutdownGracefully();


    }
}
