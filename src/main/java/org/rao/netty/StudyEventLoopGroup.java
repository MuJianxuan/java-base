package org.rao.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author Rao
 * @Date 2021/11/02
 **/
public class StudyEventLoopGroup {
    public static void main(String[] args) {

        // io 、 普通任务 、定时任务
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        // 普通任务 、定时任务
//        DefaultEventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();

        // 通过 next 获取 执行对象
//        nioEventLoopGroup.next().execute( () -> {
//            System.out.println("ok");
//        });

        // channel 和 EventLoop 对象绑定后，就会一直这个 对象负责这个channel的io操作

        // 执行定时任务   schedule  eventLoopGroup 实现了 ScheduledExecutorService 接口 ，因此 和定时任务线程池使用方式是差不多的。
//        nioEventLoopGroup.scheduleAtFixedRate(...)



        // 读取Io操作交给其他线程池
        new ServerBootstrap()
                .group(new NioEventLoopGroup(),new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {

                        ch.pipeline()
                                .addLast( new ChannelOutboundHandlerAdapter(){
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        System.out.println("4");
                                        super.write(ctx, msg, promise);
                                    }
                                })
                                .addLast( new ChannelOutboundHandlerAdapter(){
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        System.out.println("5");
                                        super.write(ctx, msg, promise);
                                    }
                                })
                                // 用默认的线程池
                                .addLast( new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

                                        System.out.println("1");

                                        // 需要把消息 传递下去  父类默认调用
//                                        ctx.fireChannelRead( msg);
                                        super.channelRead(ctx, msg);

                                    }
                                })
                                // 用外部的线程池
                                .addLast( nioEventLoopGroup,new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println("2");

                                        // 不写数据 只会执行读入的 handler

                                        // 特殊点  channel 写数据和 ctx写数据
                                        // 127654  3没执行  调用此方法 会找channel关联的所有写出handler执行
                                        ch.writeAndFlush("hello work!");

                                        // 1254 调用 此方法 会往回找 相关写出的 handler 进行调用，
//                                        ctx.writeAndFlush("hello work!");

                                        // 不调用此方法 后续的 读入 handler不会执行   意味着 3不会执行
                                        super.channelRead(ctx, msg);

                                        // 所以 ctx.writeAndFlush 与 ch.writeAndFlush 的区别是  scope（作用域）不同
                                        // ctx 是处于当前的 来写出数据，意味着到此就结束调用了 所以就执行往回了
                                        // ch 是client 的代名，意味着是客户端写出数据，如果有继续交给下一个handler 的话还是会继续执行，否则就直接找后续的 写出 handler 调用再往回找写出的handler
                                        // 在netty中 channelHandler 分为 head 和 tail， ch和ctx的区别就是
                                        // 是执行完读入的handler后直接跳到 tail后往回找写出的handler执行 还是 直接回头找 写出的handler执行。
                                    }
                                })
                                // 用外部的线程池
                                .addLast( nioEventLoopGroup,new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println("3");
                                        // 特殊点  channel 写数据和 ctx写数据
                                        // 会调用后置的  handler
                                        // 不会调用 后置的 handler
//                                        ctx.writeAndFlush("hello work!");

                                        super.channelRead(ctx, msg);

                                    }
                                })
                                .addLast( new ChannelOutboundHandlerAdapter(){
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        System.out.println("6");
                                        super.write(ctx, msg, promise);
                                    }
                                })
                                .addLast( new ChannelOutboundHandlerAdapter(){
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        System.out.println("7");
                                        super.write(ctx, msg, promise);
                                    }
                                })
                        ;


                        // 执行顺序 分析
                        // 1 >> 2 >> 4 >> 3  这里需要注意的是 写出数据是反过来，找 tail 节点处理的，为什么这么弄呢？
                        // 再多加一个 分析 第二个写出数据 验证猜想  12354  说明还是会执行 3的，那他为什么要反过来执行 写出呢？
                        // 思考： 我们是顺序添加 handler的， 如果我们尝试反过来呢？  12354 那么为什么 要反过来执行呢？ 都后面我学习到了再来补充
                        // 127654  12是读入  7654是写出

                    }
                })
                .bind(8888);





    }
}
