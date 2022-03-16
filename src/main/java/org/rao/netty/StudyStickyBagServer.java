package org.rao.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * 协议
 *    解码器，分隔符解码器 （DelimiterBasedFrameDecoder） 定长解码器 （FixedLengthFrameDecoder ） ，长度字段解码 （LengthFieldBasedFrameDecoder）
 *
 * 粘包 & 半粘包
 *   计算机网络 网卡接收数据
 *   基于滑动窗口来接收数据，
 *     计网： TCP的稳定性中提到，滑动窗口和流速控制
 *     也就是常说的限流机制，在一个窗口呢，规定了能接收的连接，
 *
 *
 *   粘包和半粘包只会出现在 TCP 层面
 *    粘包：
 *       - 应用层：1、服务端接收缓冲区大于 客户端的发送ByteBuf缓冲区，导致客户端多次传输的数据，服务端一次就接收完了；
 *       - 网络传输层：1、假设发送方 256 bytes 表示一个完整报文，但由于接收方处理不及时且窗口大小足够大（大于256 bytes），这 256 bytes 字节就会缓冲在接收方的滑动窗口中，当滑动窗口中缓冲了多个报文就会粘包
 *                   2、Nagle 算法：会造成粘包
 *    半粘包：
 *       - 应用层：1、服务端接收缓冲区 小于 客户端的发送ByteBuf缓冲区，导致 客户端一次发送传输的数据，服务需要多次来接收；
 *       - 网络传输层：1、假设接收方的窗口只剩了 128 bytes，发送方的报文大小是 256 bytes，这时接收方窗口中无法容纳发送方的全部报文，发送方只能先发送前 128 bytes，等待 ack 后才能发送剩余部分，这就造成了半包
 *       - 数据链路层：1、网卡层，MSS限制，当发送的数据超过MSS限制后，会将数据切分发送，就会造成半包。
 *     本质：
 *     TCP 是流式协议，消息无边界。
 *
 * @author Rao
 * @Date 2021/11/08
 **/
@Slf4j
public class StudyStickyBagServer {

    public static void send(ByteBuf byteBuf,String content){
        byte[] bytes = content.getBytes();
        byteBuf.writeInt( bytes.length);
        byteBuf.writeBytes( bytes);

    }

    public static void main(String[] args) {

        // 协议类型的 粘包拆包处理
//        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
//                new LengthFieldBasedFrameDecoder(1024, 0, 4, 0, 0),
//                new LoggingHandler(LogLevel.DEBUG)
//        );
//
//
//        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer();
//        send( byteBuf,"hello word!");
//        send(byteBuf,"hi");
//
//        embeddedChannel.writeInbound( byteBuf);


        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

        /**
         * 空闲检测
         *   连接假死：
         *    添加IdleStateHandler 对空闲时间进行检测，通过构造函数可以传入三个参数 为0 则不生效
         *     readerIdleSeconds 读空闲时间 超过则会产生事件
         *     writeIdleSeconds 写空闲时间 超过则会参生事件
         *     allIdleTimeSeconds 读写空闲时间
         */

        new ServerBootstrap()
                .group( nioEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                // 全局配置 >>  接收缓冲区 大小 影响的底层接收缓冲区（即滑动窗口）大小，仅决定了 netty 读取的最小单位，netty 实际每次读取的一般是它的整数倍
//                .option(ChannelOption.SO_RCVBUF,10)
                // 针对 channel 进行配置  接收缓存区 分配器  >>  设置成 定长  这种是设置 这种 是全局的
//                .childOption( ChannelOption.RCVBUF_ALLOCATOR,new FixedRecvByteBufAllocator(20))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {

                        // 用于空闲连接的检测，5s内未读到数据，会触发READ_IDLE事件
                        // 需要加上编解码器  否则代表消息未被读取 也就不会触发读空闲  因为一直未被 读取
//                        ch.pipeline().addLast( new IdleStateHandler( 5,0,0 ) );
//                        ch.pipeline().addLast( new ChannelDuplexHandler(){
//                            @Override
//                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//
//                                IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
//                                if( idleStateEvent.state() == IdleState.READER_IDLE){
//                                    // 关闭连接
//                                    ctx.channel().close();
//                                }
//
//                                super.userEventTriggered(ctx, evt);
//                            }
//                        });


                        // 添加日志打印
                        ch.pipeline().addLast( new LoggingHandler(LogLevel.DEBUG));
                        // 这种解码是 在 channel 中的 实际上 还是发生了粘包，但是呢，这边使用这个 来分割。
                        ch.pipeline().addLast( new FixedLengthFrameDecoder(20));
                        ch.pipeline().addLast( new StringDecoder());
                        ch.pipeline().addLast( new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                // 客户端连接触发
                                super.channelActive(ctx);
                            }

                            @Override
                            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                                // 客户端断开连接
                                super.channelInactive(ctx);
                            }
                        });
                    }
                })
                .bind( 8888);


    }

}
