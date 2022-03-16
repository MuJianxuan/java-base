package org.rao.netty.rao;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author Rao
 * @Date 2021/11/29
 **/
public class NettyServer {

    /**
     * 自定义 协议编码解码器
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        StringEncoder stringEncoder = new StringEncoder();
        StringDecoder stringDecoder = new StringDecoder();

        ServerBootstrap serverBootstrap = new ServerBootstrap()
                // 服务端使用的 是这个
                .channel(NioServerSocketChannel.class)
                .group(eventLoopGroup)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                        ch.pipeline().addLast(stringEncoder)
                                .addLast(stringDecoder).addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                // 读取客户端的数据
                                super.channelRead(ctx, msg);
                            }
                        });

                    }
                });


        ChannelFuture server = serverBootstrap.bind(8888).sync();


    }

}
