package org.rao.net.nio.heima;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * @author Rao
 * @Date 2021-10-22
 **/
public class HeiMaClient {

    public static void main(String[] args) throws Exception {

        // 客户端连接
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 8888));

        socketChannel.write(Charset.defaultCharset().encode("i am client!"));

        String nextLine = new Scanner(System.in).nextLine();

        // 我怎么知道客户端一次性写完了没
//        ByteBuffer byteBuffer = ByteBuffer.allocate(16);
        // 默认是阻塞的
//        int count = 0;

//        while (count < 500000) {
//            count += socketChannel.read( byteBuffer);
//
//            System.out.println(" server msg:" + new String( byteBuffer.array()));
//
//            byteBuffer.clear();
//            System.out.println("count:" + count);
//        }
//        socketChannel.write(Charset.defaultCharset().encode("client is me!"));


        // 打印读出来的总值

//        System.out.println("server msg:" + new String(byteBuffer.array()));

//        socketChannel.configureBlocking(false);

//        int count = 2;
//        while (count-- > 0){
//            socketChannel.write(Charset.defaultCharset().encode("i am client!"));
//
//            Thread.sleep(1000);
////            ByteBuffer byteBuffer = ByteBuffer.allocate(50);
////            // 默认是阻塞的
////            socketChannel.read( byteBuffer);
////            System.out.println("server msg:" + new String(byteBuffer.array()));
//        }

        // 主动断开
//        socketChannel.close();
    }



}
