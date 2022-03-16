package org.rao.net.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Paths;

/**
 * @author Rao
 * @Date 2021-11-01
 **/
public class AioStudy {
    public static void main(String[] args) throws Exception {

        try (AsynchronousFileChannel asynchronousFileChannel = AsynchronousFileChannel.open(Paths.get("data.txt"))) {

            ByteBuffer byteBuffer = ByteBuffer.allocate(26);

            System.out.println("read ............ start ");

            asynchronousFileChannel.read(byteBuffer, 0, byteBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                @Override
                public void completed(Integer result, ByteBuffer attachment) {
                    System.out.println( "read .....: " + new String( attachment.array()));
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    exc.printStackTrace();
                    System.out.println( "read .....: " + new String( attachment.array()));
                }
            });

        };

        System.out.println("read ............ end ");

        Thread.sleep(2000);

    }
}
