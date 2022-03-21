package org.rao.netty.rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * desc: netty client utils
 *
 * @author Rao
 * @Date 2022/03/21
 **/
@Slf4j
public class BootstrapClientUtils {

    /**
     * 存储管道map
     */
    private static final Map<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>();


    /**
     *  ? 是否要使用全局线程池呢?
     */
    private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
    /**
     * 获取管道
     * @param urlKey
     * @param responsePromise
     * @return
     */
    public static Channel getChannel(String urlKey, DefaultPromise<String> responsePromise) {


        Channel channel = CHANNEL_MAP.get(urlKey);
        if(channel != null) {
            return channel;
        }

        try {
            synchronized ( CHANNEL_MAP ) {

                channel = CHANNEL_MAP.get(urlKey);
                if(channel != null) {
                    return channel;
                }

                Bootstrap bootstrap = new Bootstrap();
                ChannelFuture connect = bootstrap.group( nioEventLoopGroup)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) throws Exception {
                                ch.pipeline().addLast(new StringDecoder());
                                ch.pipeline().addLast(new StringEncoder());
                                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        String responseJson = (String) msg;
                                        log.info("response:{}",responseJson);
                                        // 怎么样返回呢？
                                        responsePromise.setSuccess( responseJson );
                                    }
                                });
                            }
                        })
                        .connect("localhost", 8080);

                return connect.sync().channel();
            }

        } catch (Exception ex){
            log.error("连接服务器失败！");
        }
        throw new RuntimeException("连接失败！");

    }

    /**
     * 销毁
     */
    public static void destroyChannels(){
        synchronized ( CHANNEL_MAP){
            CHANNEL_MAP.forEach((k,c) -> {
                c.close();
            });
        }
    }


}
