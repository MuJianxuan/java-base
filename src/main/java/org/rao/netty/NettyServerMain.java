package org.rao.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.charset.Charset;

/**
 * @author Rao
 * @Date 2021-11-01
 **/
public class NettyServerMain {

    public static void main(String[] args) {

        // 链路： 初始化 ServerBootStrap >> 设置事件组 new NioEventLoopGroup >> 设置管道类型 NioServerSocketChannel
        // >> 添加处理 借助管道初始器[为管道添加拦截处理器 new StringDecoder 解码器，入站简单处理器 new SimpleChannelInboundHandler ]
        // >> 绑定端口
        new ServerBootstrap()
                // 添加 事件组 使用的是 nio 事件循环组
                /**
                 * EventLoop 事件循环对象： 具体干活的工人， 本质是 一个单线程执行器(同时维护了一个Selector) 里面有 run方法处理Channel上源源不断的io事件
                 * EventLoopGroup 事件循环对象组  一群干活的工人  Netty是基于 Reactor设计是实现的；
                 * 这里应该还需划分 boss 和 worker 对应 Reactor 主从线程模式。
                 */
                .group(new NioEventLoopGroup())
//                .option( ChannelOption.CONNECT_TIMEOUT_MILLIS,)
                // 建立管道类型
                .channel(NioServerSocketChannel.class)
                // 这个泛型对象不是 NioServerSocketChannel 而是 NioSocketChannel
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        ch.pipeline().addLast( new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast( new StringDecoder());
                        ch.pipeline().addLast( new StringEncoder());
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                System.out.println("msg:" + msg);

                                ctx.writeAndFlush( msg+ "_server ok!");
                            }
                        });


                        // 自定义处理数据
//                        ch.pipeline().addLast( new ChannelInboundHandlerAdapter(){
//
//                            @Override                                         // 由于没有前置的 StringDecoder 解码器，因此需要自己解码  当对象是 ByteBuf
//                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//                                ByteBuf byteBuf = (ByteBuf) msg;
//                                // 实际开发中应该要指定  编码格式
//                                System.out.println( byteBuf.toString(Charset.defaultCharset()));
//                            }
//                        });




                    }
                })
                .bind(8888);

    }

}
