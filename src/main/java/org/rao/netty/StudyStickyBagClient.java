package org.rao.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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

/**
 * 粘包的问题
 * @author Rao
 * @Date 2021/11/08
 **/
public class StudyStickyBagClient {

    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

        // 客户端
        new Bootstrap()
                .group( nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

//                        /**
//                         * 添加3秒空闲写事件
//                         */
//                        ch.pipeline().addLast( new IdleStateHandler( 0,3,0) );
//                        ch.pipeline().addLast( new ChannelDuplexHandler(){
//                            @Override
//                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//
//                                IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
//                                // 空闲写事件
//                                if( idleStateEvent.state() == IdleState.WRITER_IDLE){
////                                    ctx.channel().writeAndFlush( );????  没找到
////                                    ctx.channel().writeAndFlush( new PingMessage());
//
//                                }
//
//                                super.userEventTriggered(ctx, evt);
//                            }
//                        });

                        ch.pipeline().addLast( new LoggingHandler( LogLevel.DEBUG));
                        ch.pipeline().addLast( new StringEncoder() );
                        ch.pipeline().addLast( new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                // 客户端连接
                                // 每次发送16个字节的数据，共发送10次
                                for (int i = 0; i < 10; i++) {
                                    String msg = "hello server ,i am " + i;
                                    ctx.writeAndFlush( msg );
                                }

                            }
                        });

                    }
                })
                .connect("localhost",8888).sync();



    }

}
