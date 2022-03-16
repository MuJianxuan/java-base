package org.rao.net.bio;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Rao
 * @Date 2021-10-21
 **/
public class BioClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("127.0.0.1", 8080);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter printWriter = new PrintWriter( new OutputStreamWriter( socket.getOutputStream()));
        ){

            new Thread(() -> {
                try (Scanner scanner = new Scanner(System.in);) {
                    String line;
                    while (!"88".equals((line = scanner.nextLine()))){
                        printWriter.println(line);
                        printWriter.flush();
                    }
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            try {
                String readLine;
                while ( (readLine = bufferedReader.readLine() )!= null && ! socket.isClosed()){
                    System.out.println("server msg:"+readLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
