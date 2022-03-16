package org.rao.netty.idle;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @author Rao
 * @Date 2021/12/22
 **/
@Slf4j
public class IdleServer {

    public static void main(String[] args) {

        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        ChannelFuture server = new ServerBootstrap()
                .group(eventExecutors)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                        // 服务端 当前channel 5s 读空闲
                        ch.pipeline().addLast( new IdleStateHandler( 5,0,0 ));
                        ch.pipeline().addLast( new LoggingHandler( LogLevel.DEBUG));
                        // 必须加上编解码器 否则不会生效
                        ch.pipeline().addLast( new StringDecoder());
                        ch.pipeline().addLast( new ChannelDuplexHandler(){
                            @Override
                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

                                log.info("event {}", JSON.toJSONString( idleStateEvent ));

                                // 如果是 读空闲
                                if( idleStateEvent.state() == IdleState.READER_IDLE ){
                                    // 断开连接
                                    ctx.channel().close();
                                }
                            }
                        });
                        ch.pipeline().addLast( new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                log.info( "client msg：" + msg );
                                super.channelRead(ctx, msg);
                            }
                        });



                    }
                })
                .bind(8888);
    }

}
