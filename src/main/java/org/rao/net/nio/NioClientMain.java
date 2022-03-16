package org.rao.net.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * 客户端
 * @author Rao
 * @Date 2021-10-22
 **/
public class NioClientMain {
    public static void main(String[] args) throws Exception {
        // 连接
        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1",8888));
        socketChannel.configureBlocking(false);

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try (Scanner scanner = new Scanner(System.in)){
            String line;
            while ((line = scanner.nextLine()) != null){

                buffer.put( line.getBytes());
                buffer.flip();

                socketChannel.write( buffer);
                buffer.clear();
            }

        }


    }
}
