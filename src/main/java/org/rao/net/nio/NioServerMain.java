package org.rao.net.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * 服务端
 * @author Rao
 * @Date 2021-10-21
 **/
public class NioServerMain {

    public static void main(String[] args) throws Exception {

        /**
         * 所有的系统I/O都分为两个阶段：等待就绪和操作。举例来说，读函数，分为等待系统可读和真正的读；同理，写函数分为等待网卡可以写和真正的写。
         * 换句话说，BIO里用户最关心“我要读”，NIO里用户最关心”我可以读了”，在AIO模型里用户更需要关注的是“读完了”。
         */

        // Selector
        Selector selector = Selector.open();

        // channel ?
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking( false);
        serverSocketChannel.bind( new InetSocketAddress(8888) );
        // 注册 channel 到 selector 事件选择器上
        serverSocketChannel.register( selector, SelectionKey.OP_ACCEPT);

        // 存在已就绪的事件
        while ( selector.select() > 0){

            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while ( iterator.hasNext() ){
                // 判断事件类型
                SelectionKey selectionKey = iterator.next();

                // 可连接
                if( selectionKey.isAcceptable()){
                    System.out.println("isAcceptable");
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    // 注册读事件  猜想： 是不是不注册的事件永远不会发生？ 怎么通过回调方式设置 相关事件
                    socketChannel.register( selector,SelectionKey.OP_READ);
                }
                // 可读事件
                else if( selectionKey.isReadable()){
                    System.out.println("isReadable");
                    // 转为 socket 通道
                    SocketChannel socketChannel = (SocketChannel)selectionKey.channel();

                    // 非直接分配  也就是会存在 堆中
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int len;
                    // 数据会写在 socketChanel 中 多次的数据会写在一起， 这里就会导致一个问题是 请求数据对不上！
                    //  如果别人发的消息太快，而你处理太慢，数据就会重合，这个时候就需要拆了
                    while ( (len = socketChannel.read( buffer)) > 0){
                        // 读取
                        buffer.flip();
                        System.out.println("client msg: "+ new String( buffer.array(),0,len));
                        buffer.clear();
                    }

                    // 模拟 业务执行
                    System.out.println("will Thread.sleep(10000)...........");
                    Thread.sleep(10000);
                    System.out.println("Thread.sleep(10000)...........");

                }
                // 移除当前事件
                iterator.remove();

            }

        }

    }

}
