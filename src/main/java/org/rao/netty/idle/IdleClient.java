package org.rao.netty.idle;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

/**
 * idle 是空闲的意思
 * @author Rao
 * @Date 2021/12/22
 **/
@Slf4j
public class IdleClient {

    public static void main(String[] args) {

        // 还许哟解决粘包半粘包问题
        ChannelFuture client = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                        // 当前channel 3s 写空闲
                        ch.pipeline().addLast(new IdleStateHandler(0, 2, 0));
                        ch.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        ch.pipeline().addLast( new StringEncoder());
                        ch.pipeline().addLast(new ChannelDuplexHandler() {
                            @Override
                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

                                IdleStateEvent idleStateEvent = (IdleStateEvent) evt;

                                log.info("event {}", JSON.toJSONString(idleStateEvent));

                                if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                                    // 随便写个空消息过去
                                    ctx.writeAndFlush("1111");
                                }

                            }
                        });

                    }
                })
                .connect("127.0.0.1", 8888);

    }

}
