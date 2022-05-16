package com.webserver.core;

import com.webserver.http.HttpServletRequest;

import java.io.*;
import java.net.Socket;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 该任务负责与指定的客户端进行HTTP交互
 */
public class ClientHandler implements Runnable{
    private Socket socket;

    public ClientHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            //1 解析请求
            HttpServletRequest request = new HttpServletRequest(socket);

            //2 处理请求


            //3 发送响应
            //这里相当于定位的是当前项目下的resources目录
            File rootDir = new File(
                    ClientHandler.class.getClassLoader()
                            .getResource(".").toURI()
            );
            /*
                定位resources下的static目录
                注:resources下的static目录是sprint boot项目中用于存放所有静态资源
                  的目录，相当于我们写的"网站"中用到的页面，图片等资源都放在static下
             */
            File staticDir = new File(rootDir,"static");

            //定位页面: resources/static/myweb/index.html
            File file = new File(staticDir,"/myweb/index.html");

            /*
                HTTP/1.1 200 OK(CRLF)
                Content-Type: text/html(CRLF)
                Content-Length: 2546(CRLF)(CRLF)
                1011101010101010101......
             */
            //发送状态行
            println("HTTP/1.1 200 OK");

            //发送响应头
            println("Content-Type: text/html");
            println("Content-Length: "+file.length());
            println("");

            //发送响应正文
            FileInputStream fis = new FileInputStream(file);
            OutputStream out = socket.getOutputStream();
            byte[] data = new byte[1024*10];//10kb
            int len;//记录每次实际读取到的字节数
            while((len = fis.read(data))!=-1){
                out.write(data,0,len);
            }

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void println(String line) throws IOException {
        OutputStream out = socket.getOutputStream();
        byte[] data = line.getBytes(StandardCharsets.ISO_8859_1);
        out.write(data);
        out.write(13);
        out.write(10);
    }


}
