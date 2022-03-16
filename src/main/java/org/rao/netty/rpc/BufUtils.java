package org.rao.netty.rpc;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

/**
 * desc: 工具
 *
 * @author Rao
 * @Date 2022/01/21
 **/
public class BufUtils {

    /**
     * 为什么要自定义协议
     * @param byteBuf
     * @param msg
     * @return
     */
    public static void writeContent(ByteBuf byteBuf, String msg ){

        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        byteBuf.writeInt( bytes.length );
        byteBuf.writeBytes( bytes );

    }

}
