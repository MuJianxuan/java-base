package org.rao.netty.rpc;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultPromise;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.rao.netty.rpc.msg.RpcResponseMessage;

import java.util.Map;
import java.util.Optional;
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
     * 删除时机呢？
     */
    private static final Map<String, DefaultPromise<RpcResponseMessage>> DEFAULT_PROMISE_MAP = new ConcurrentHashMap<>();

    /**
     *  ? 是否要使用全局线程池呢?
     */
    private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

    /**
     * 获取管道 ???
     * @param urlKey
     * @param reqId
     * @param responsePromise
     * @return
     */
    public static Channel getChannel(String urlKey, String reqId, DefaultPromise<RpcResponseMessage> responsePromise) {

        DEFAULT_PROMISE_MAP.put(reqId,responsePromise);

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

                                        RpcResponseMessage rpcResponseMessage = JSON.parseObject(responseJson, RpcResponseMessage.class);

                                        // 怎么样返回呢？  如果超时了，还是会写入，所以，这里还是有问题的
                                        Optional.ofNullable( DEFAULT_PROMISE_MAP.get( rpcResponseMessage.getReqId() )).ifPresent(promise -> {
                                            promise.setSuccess( rpcResponseMessage);
                                        });


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
     * 清除reqId绑定的数据
     * @param reqId
     */
    public static void clearDefaultPromise(String reqId){
        DEFAULT_PROMISE_MAP.remove(reqId);
    }

    /**
     * 创建Channel  性能比较
     * @return
     * @param responsePromise
     */
    @SneakyThrows
    public static Channel createChannel(DefaultPromise<RpcResponseMessage> responsePromise) {
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
                                RpcResponseMessage rpcResponseMessage = JSON.parseObject(responseJson, RpcResponseMessage.class);

                                // 怎么样返回呢？
                                responsePromise.setSuccess(rpcResponseMessage);

                            }
                        });
                    }
                })
                .connect("localhost", 8080);

        return connect.sync().channel();
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
