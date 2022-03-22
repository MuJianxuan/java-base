package org.rao.netty.rpc.msg;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rao
 * @Date 2021/12/23
 **/
@Setter
@Getter
@ToString
public class RpcRequestMessage {

    private String reqId;
    /**
     * 调用的接口全限定名，服务端根据它找到实现
     */
    private String interfaceName;

    /**
     * 调用接口中的方法名
     */
    private String methodName;

    /**
     * 方法返回类型 ? 似乎用不到
     */
    private Class<?> returnType;

    /**
     * 方法参数类型数组
     */
    private Class<?>[] parameterTypes;

    /**
     * 方法参数值数组
     */
    private Object[] parameterValue;

    public RpcRequestMessage() {
    }

    public RpcRequestMessage(String interfaceName, String methodName, Class<?> returnType, Class<?>[] parameterTypes, Object[] parameterValue) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
        this.parameterValue = parameterValue;
    }

}