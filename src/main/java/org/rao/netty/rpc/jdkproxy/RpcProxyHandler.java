package org.rao.netty.rpc.jdkproxy;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;
import org.rao.netty.rpc.BootstrapClientUtils;
import org.rao.netty.rpc.msg.RpcRequestMessage;
import org.rao.netty.rpc.msg.RpcResponseMessage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * 功能点：
 * 1、发起远程调用获取结果
 * 2、过滤器等功能实现
 * 3、设计dubbo机制的过滤链
 * 4、
 * @author Rao
 * @Date 2022/01/08
 **/
@Slf4j
public class RpcProxyHandler implements InvocationHandler {

    private final Class<?> cls;

    public RpcProxyHandler( Class<?> cls) {
        this.cls = cls;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 获取要发送的消息
        RpcRequestMessage rpcRequestMessage = new RpcRequestMessage(cls.getSimpleName(), method.getName(), method.getReturnType(), method.getParameterTypes(), args);
        String rpcMsg = JSON.toJSONString(rpcRequestMessage);
        // 发送消息给 server端
        //创建一个承诺
        DefaultPromise<String> responsePromise = new DefaultPromise<String>(new NioEventLoopGroup().next());

        try {
            // 这一部分应该 重构，优化创建，应该存在则不创建 --> ip:端口  ::  channel
            Channel channel = BootstrapClientUtils.getChannel("localhost:8080",responsePromise);
            channel.writeAndFlush(rpcMsg );

            // 等待响应 10s
            String responseJson = responsePromise.get(10, TimeUnit.SECONDS);
            RpcResponseMessage rpcResponseMessage = JSON.parseObject(responseJson, RpcResponseMessage.class);

            if( rpcResponseMessage.getCode() == 200 ){
                // 结果转换返回
                return rpcResponseMessage.getData();
            }
            throw new Exception( rpcResponseMessage.getThrowable() );
        } catch (Exception ex) {
            // if 是请求超时

            // 或者是 获取等待超时

            throw ex;

        }
    }
}
