package org.rao.file;

import java.io.*;

/**
 * @author Rao
 * @Date 2021-10-22
 **/
public class FileMain {
    public static void main(String[] args) throws Exception {

        // 写文件  写到 jar 包中去了....

        // 这一段有问题  .getPath 调用对象可能为 null
        File file = new File( FileMain.class.getClassLoader().getResource("ddd.txt").getPath() );
        if (! file.exists()) {
            System.out.println("文件不存在！");
            return;
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));){
            // 会覆盖源文件的 内容
            bufferedWriter.write("you can!");
            bufferedWriter.flush();
        }

        //  / 是从 classpath:下读取  不加 / 默认从项目的路径上读取。
        try (InputStream inputStream = FileMain.class.getResourceAsStream("/ddd.txt");
             BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            // 第一个能读到
            System.out.println("bufferedReader1" + bufferedReader1.readLine() );
        }

    }
}
