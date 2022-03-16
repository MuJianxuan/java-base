package org.rao.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

/**
 * @author Rao
 * @Date 2021/12/22
 **/
@Slf4j
public class EncoderStudy {

    public static void main(String[] args) {

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(
                // 最大长度，长度值偏移量，长度字段长度，长度与有用数据偏移量，数据读取起点
                new LengthFieldBasedFrameDecoder(2, 0, 4, 0, 4),
                new LoggingHandler(LogLevel.DEBUG),
                new StringEncoder(),
                new StringDecoder(),
                new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        log.info("[msg] " + msg);
                        super.channelRead(ctx, msg);
                    }
                }

        );

        // 模拟写入数据

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        send( buffer,"Hello ");
        embeddedChannel.writeInbound( buffer);

        buffer.clear();
        buffer.resetWriterIndex();
        ByteBuf buffer1 = ByteBufAllocator.DEFAULT.buffer();
        send( buffer,"Word!");
        embeddedChannel.writeInbound( buffer);

    }

    private static void send(ByteBuf buf, String msg) {
        // 得到数据的长度
        int length = msg.length();
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        // 将数据信息写入buf
        // 写入长度标识前的其他信息
//        buf.writeByte(0xCA);
        // 写入数据长度标识
        buf.writeInt(length);
        // 写入长度标识后的其他信息
//        buf.writeByte(0xFE);
        // 写入具体的数据
        buf.writeBytes(bytes);
    }


}
