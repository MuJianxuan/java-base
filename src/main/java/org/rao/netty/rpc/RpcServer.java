package org.rao.netty.rpc;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.rao.netty.rpc.msg.RpcRequestMessage;
import org.rao.netty.rpc.msg.RpcResponseMessage;
import org.rao.netty.rpc.ref.HelloService;
import org.rao.netty.rpc.ref.HelloServiceImpl;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * RPC 服务端
 * @author Rao
 * @Date 2021/12/21
 **/
@Slf4j
public class RpcServer {

    private static Map<String, HelloService> beanMap = new HashMap<>();

    static {
        beanMap.put( HelloService.class.getSimpleName(),new HelloServiceImpl() );
    }

    public static void main(String[] args) throws Exception{

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

        // rpc 消息的这个值设置多大呢?  还是使用 \n 解码器呢？ 心跳包检测呢？ 使用json是方便，性能上却差些。


        ChannelFuture channelFuture = serverBootstrap
                .group(nioEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override

                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new StringDecoder());
                        ch.pipeline().addLast( new LengthFieldBasedFrameDecoder( 8192, 0, 4, 0, 4) );
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                RpcResponseMessage rpcResponseMessage = new RpcResponseMessage();
                                String requestJson = (String) msg;
                                log.info("server receive :"+requestJson);
                                RpcRequestMessage rpcRequestMessage = JSON.parseObject(requestJson, RpcRequestMessage.class);
                                try {
                                    HelloService helloService = beanMap.get(rpcRequestMessage.getInterfaceName());
                                    Method method = helloService.getClass().getMethod(rpcRequestMessage.getMethodName(), rpcRequestMessage.getParameterTypes());
                                    Object invoke = method.invoke(helloService, rpcRequestMessage.getParameterValue());
                                    rpcResponseMessage.setData(invoke);
                                } catch (Exception ex) {
                                    rpcResponseMessage.setCode(500);
                                    rpcResponseMessage.setThrowable(ex);
                                }
                                // 写出数据
                                rpcResponseMessage.setReqId( rpcRequestMessage.getReqId());
                                ctx.channel().writeAndFlush( JSON.toJSONString(rpcResponseMessage) );
                                super.channelRead(ctx, msg);
                            }
                        });
                        ch.pipeline().addLast(new StringEncoder());

                    }
                })
                .bind(8080).sync();
    }

//    public static String sendMsg(ByteBuf byteBuf,){
//
//
//
//    }

}


