package org.rao.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 * @author Rao
 * @Date 2021-11-02
 **/
public class NettyClientMain {
    public static void main(String[] args) throws Exception {

        // 优雅关闭
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();

        // client 标准写法
        ChannelFuture clientFuture = new Bootstrap()
                // 添加拦截器
                .group( eventExecutors)
                // 设置管道类型
                .channel(NioSocketChannel.class)
                // 添加拦截器   我感觉这个 Channel的类型需要和 服务端保持一致。
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        // 添加 String 解析器   添加编码器  要解析 服务端发过来的还需要 解码器 ，服务端也同理。
                        ch.pipeline()
                                .addLast(new StringEncoder())
                                .addLast(new StringDecoder())
                                .addLast( new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println("server smg: " + msg );
                                        super.channelRead(ctx, msg);
                                    }
                                })
                        ;
                    }
                })
                // 添加连接地址
                .connect(new InetSocketAddress("localhost", 8888));

        // 如果不先 sync 就获取 channel的话是一个 未建立好的channel

        //方式1获取channel 同步等待建立 channel连接
        Channel channel = clientFuture.sync()
                // 获取channel对象
                .channel();

        channel.writeAndFlush("1");
        channel.writeAndFlush("2");

//        CountDownLatch countDownLatch = new CountDownLatch(4);
//        List<Thread> threads = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//
//            int finalI = i;
//            threads.add( new Thread(() -> channel.writeAndFlush(finalI)) );
//
//            countDownLatch.countDown();
//        }
//
//        countDownLatch.await();
//
//        threads.forEach(Thread::start);

        /**
         * 在整个 netty中，通过 ChannelFuture 实现调用的方法几乎都是异步的，在整体设计中，netty 对线程池的掌控是很棒的。
         */
        // 方式2获取channel
//        clientFuture.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture future) throws Exception {
//                // 通过调用监听器 从而获取到
//                Channel acceptedChannel = future.channel();
//            }
//        });

        System.out.println(channel);

        new Thread(() ->{
            Scanner scanner = new Scanner(System.in);
            String line;
            while (! "1".equals( (line = scanner.nextLine()) )){
                channel.writeAndFlush(line);
            }

            channel.close();

        }).start();



        // 优雅关闭后处理
        // 获取关闭的方法
        ChannelFuture closeFuture = channel.closeFuture();
        // 优雅关闭监听
        closeFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                System.out.println("close........");
                // 优雅关闭
                eventExecutors.shutdownGracefully();
            }
        });

    }
}
