package com.webserver.controller;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
import qrcode.QRCodeUtil;

import java.io.FileOutputStream;

/**
 *  Tool 工具
 *  生成二维码，验证码等的一个业务处理类
 */
public class ToolsController {
    public void createQR(HttpServletRequest request, HttpServletResponse response){
        String line = request.getParameter("content");
        try {
            response.setContentType("image/jpeg");
            QRCodeUtil.encode(line,"./logo.jpg",response.getOutputStream(),true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws Exception {
        String line = "今天是520~~~";
        //参数1:二维码图上表示的内容   2:二维码保存的位置
//        QRCodeUtil.encode(line,"./qr.jpg");
        //参数1:内容 参数2:二维码中间的logo图 参数3:二维码保存的位置 参数4:logo图尺寸是否压缩到中间合适的尺寸上(logo图过大就需要压缩)
//        QRCodeUtil.encode(line,"./logo.jpg","./qr.jpg",true);
        System.out.println("二维码已生成");
        //参数3是一个字节输出流，作用是将生成的图片对应的数据通过该流写出
        QRCodeUtil.encode(line,"./logo.jpg",new FileOutputStream("./qr.jpg"),true);


    }
}









