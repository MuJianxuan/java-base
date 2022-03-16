package org.rao.net.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * 网络
 * @author Rao
 * @Date 2021-10-21
 **/
public class BioServerMain {

    /**
     * 改进思路： 构建数据结构对象体，在存储的时候将 Socket的相关对象解析，避免在使用过程中，重复解析
     */

    private static List<Consumer<String>> consumerList = new ArrayList<>();
    private static List<Socket> socketList = new ArrayList<>();


    public static void main(String[] args) throws Exception {

        // 这里应该要定义拒绝策略
        ExecutorService executorService = Executors.newFixedThreadPool(5);

        try (ServerSocket serverSocket = new ServerSocket(8080);) {

            // 广播
            executorService.execute( () -> {


                String str;
                // 阻塞式读取
                while ((str = new Scanner(System.in).nextLine() ) != null){
                    String finalStr = str;
//                    consumerList.forEach(consumer -> consumer.accept(finalStr));
                    socketList.forEach( socket -> {
                        try(BufferedWriter bufferedWriter = new BufferedWriter( new OutputStreamWriter( socket.getOutputStream()));) {
                            bufferedWriter.write( finalStr );
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                }

            });

            while (!Thread.currentThread().isInterrupted()){
                // 阻塞的  等待客户端连接
                Socket socket = serverSocket.accept();

                // 异步接受数据
                executorService.execute(() -> {

                    // 可以获取 socketChannel
                    SocketChannel socketChannel = socket.getChannel();

                    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                         BufferedWriter bufferedWriter = new BufferedWriter( new OutputStreamWriter( socket.getOutputStream()));
                    ){

                        consumerList.add( str -> {
                            try {
                                bufferedWriter.write( str );
                                bufferedWriter.newLine();
                                bufferedWriter.flush();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        });

                        String readLine;
                        // 阻塞式读取
                        while ( (  readLine = bufferedReader.readLine() ) != null){
                            System.out.println("client msg:"+readLine);
                            bufferedWriter.write("server shou dao!");
                            bufferedWriter.newLine();
                            bufferedWriter.flush();
                        }



                        System.out.println("end end end end");

                    }
                    catch (Exception e) {
                        if(! socket.isClosed()){
                            try {
                                socket.close();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                        e.printStackTrace();
                    }
                });
            }
        }



    }

}
