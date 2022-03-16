package org.rao.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Rao
 * @Date 2021/11/09
 **/
public class HttpServer {

    public static void main(String[] args) {

        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        new ServerBootstrap()
                .group( nioEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast( new LoggingHandler( LogLevel.DEBUG));
                        // codec 编解码器 包含编码和解码。
                        ch.pipeline().addLast( new HttpServerCodec());
                        // 只关注
                        ch.pipeline().addLast(new SimpleChannelInboundHandler<HttpRequest>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext ctx, HttpRequest httpRequest) throws Exception {
                                System.out.println( httpRequest.uri());

                                DefaultFullHttpResponse response = new DefaultFullHttpResponse(httpRequest.protocolVersion(), HttpResponseStatus.OK);
                                byte[] bytes = "Hi ,i am netty!".getBytes();
                                response.headers().add(HttpHeaderNames.CONTENT_LENGTH,bytes.length);
                                response.content().writeBytes( bytes);

                                ctx.channel().writeAndFlush( response);

                            }
                        });

                    }
                })
                .bind(8888);


    }

}
