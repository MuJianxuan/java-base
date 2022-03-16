package org.rao.netty.rpc.msg;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rao
 * @Date 2021/12/23
 **/
@ToString
@Getter
@Setter
public class RpcResponseMessage {


    /**
     * 异常信息
     */
    private Throwable throwable;

    /**
     * 返回值
     */
    private Object data;

    /**
     * 状态码
     */
    private int code = 200;



}
