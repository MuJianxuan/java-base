package org.rao.netty.msgpack;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Rao
 * @Date 2021/11/29
 **/
public class MsgPackServer {

    public static void main(String[] args) {

        // 基于 msgpack 的 粘包/半包处理

        new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                        ch.pipeline()
                                .addLast( new LoggingHandler(LogLevel.DEBUG))

                                .addLast( new LengthFieldBasedFrameDecoder(65535,0,2,0,2))
                                // 这个似乎  对length字段取值
//                                .addLast( new MsgpackDeCoder())
                        .addLast( new LengthFieldPrepender(2))
                        ;
                    }
                })
                .bind(8888);




    }

}
