package com.webserver.controller;

import com.webserver.annotation.Controller;
import com.webserver.annotation.RequestMapping;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;
import qrcode.QRCodeUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 *  Tool 工具
 *  生成二维码，验证码等的一个业务处理类
 */
@Controller
public class ToolsController {
    @RequestMapping("/myweb/createQR")
    public void createQR(HttpServletRequest request, HttpServletResponse response){
        String line = request.getParameter("content");
        try {
            response.setContentType("image/jpeg");
            QRCodeUtil.encode(line,"./logo.jpg",response.getOutputStream(),true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成验证码的
     * @param request
     * @param response
     */
    @RequestMapping("/myweb/random.jpg")
    public void createRandomImage(HttpServletRequest request, HttpServletResponse response){
        //生成一个验证码图片响应给浏览器
        /*
            生成一张图片的大致步骤:
            1:准备一张空白图片(固定图片的宽高尺寸)
            2:通过图片获取一个用于向该图片上绘制内容的画笔
            3:随机生成一个颜色并涂满图片(随机生成背景颜色)
            4:利用画笔向图片上绘制内容
              4.1:随机生成4个字符并依次绘制到图片上
              4.2:随机生成5条干扰线绘制到图片上
         */
        Random random = new Random();
        //1 RGB:红绿蓝
        BufferedImage image = new BufferedImage(70,30,BufferedImage.TYPE_INT_RGB);
        //2
        Graphics g = image.getGraphics();
        //3 background背景
        Color bgcolor = new Color(random.nextInt(256),random.nextInt(256),random.nextInt(256));
        g.setColor(bgcolor);//给画笔蘸颜色
        g.fillRect(0,0,70,30);//利用画笔给图片填满该颜色

        //4.1生成4个字符(英文数字)并依次绘制到图片上
        String line = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        g.setFont(new Font(null, Font.BOLD, 20));//画字符前给画笔设置一个字体，绘制字符时按照该字体绘制了
        for(int i=0;i<4;i++) {
            String c = line.charAt(random.nextInt(line.length())) + "";
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.drawString(c, i*15+5, 18+(random.nextInt(11)-5));//将字符串绘制到指定的坐标处
        }
        //4.2
        for(int i=0;i<5;i++) {
            g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            g.drawLine(random.nextInt(71), random.nextInt(31),
                    random.nextInt(71), random.nextInt(31));
        }
        try {
            response.setContentType("image/jpeg");
            ImageIO.write(image,"jpg",response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws Exception {
        Random random = new Random();
        String line = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int index = random.nextInt(line.length());
        String c = line.charAt(index) + "";
        System.out.println(c);
    }
}









