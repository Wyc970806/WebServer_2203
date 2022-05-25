package com.webserver.test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 测试ByteArrayOutputStream
 * java.io.ByteArrayOutputStream是一个字节输出流，且是一个低级流。
 * 该留内部维护着一个字节数组，通过这个流写出的数据实际上都保存在了内部维护的这个字节数组上。
 */
public class BAOSDemo {
    public static void main(String[] args) throws FileNotFoundException {
//        FileOutputStream fos = new FileOutputStream("./pw.txt");
//        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        try(
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(baos, StandardCharsets.UTF_8);
            BufferedWriter bw = new BufferedWriter(osw);
            PrintWriter pw = new PrintWriter(bw,true);
        ) {
            pw.println("hello");

            //想得到写出的字节内容，我们可以通过baos获取
            byte[] data = baos.toByteArray();//得到通过ByteArrayOutputStream最终写出的所有字节
            System.out.println(Arrays.toString(data));
        }catch (Exception e){

        }

    }
}
