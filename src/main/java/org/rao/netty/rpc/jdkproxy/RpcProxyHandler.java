package org.rao.netty.rpc.jdkproxy;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;
import org.rao.netty.rpc.BootstrapClientUtils;
import org.rao.netty.rpc.msg.RpcRequestMessage;
import org.rao.netty.rpc.msg.RpcResponseMessage;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
        rpcRequestMessage.setReqId( UUID.fastUUID().toString() );
        String rpcMsg = JSON.toJSONString(rpcRequestMessage);
        // 发送消息给 server端
        //创建一个承诺
        DefaultPromise<RpcResponseMessage> responsePromise = new DefaultPromise<RpcResponseMessage>(new NioEventLoopGroup().next());
        String reqId = rpcRequestMessage.getReqId();
        try {
            // 这一部分应该 重构，优化创建，应该存在则不创建 --> ip:端口  ::  channel
            //Channel channel = BootstrapClientUtils.getChannel("localhost:8080",reqId,responsePromise);
            Channel channel = BootstrapClientUtils.createChannel(responsePromise);
            channel.writeAndFlush(rpcMsg );

            // 等待响应 10s
            RpcResponseMessage rpcResponseMessage = responsePromise.get(10, TimeUnit.SECONDS);
            BootstrapClientUtils.clearDefaultPromise(reqId);
            if( rpcResponseMessage.getCode() == 200 ){
                // 结果转换返回
                return rpcResponseMessage.getData();
            }
            throw new Exception( rpcResponseMessage.getThrowable() );
        } catch (Exception ex) {
            // if 是请求超时

            // 或者是 获取等待超时
            if( ex instanceof TimeoutException){
                log.warn("请求超时！");
                return null;
            }

            throw ex;

        }
    }
}
