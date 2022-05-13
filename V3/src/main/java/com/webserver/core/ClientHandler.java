package com.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

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
            InputStream in = socket.getInputStream();
            int d;
            while((d = in.read()) != -1){
                System.out.print((char)d);
            }




        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
