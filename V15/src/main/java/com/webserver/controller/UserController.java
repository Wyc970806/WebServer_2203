package com.webserver.controller;

import com.webserver.entity.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * 处理与用户相关操作的业务类
 */
public class UserController {
    private static File userDir;//表示保存所有用户信息的目录users
    static {
        userDir = new File("./users");
        if(!userDir.exists()){
            userDir.mkdirs();
        }
    }

    /**
     * 处理用户注册业务
     * @param request
     * @param response
     */
    public void reg(HttpServletRequest request, HttpServletResponse response){
        //1通过request获取reg.html页面上表单里用户输入的信息
        //获取参数时，getParameter传入的参数要与页面表单中对应输入框的名字一致(输入框name属性的值)
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String nickname = request.getParameter("nickname");
        String ageStr = request.getParameter("age");
        System.out.println(username+","+password+","+nickname+","+ageStr);
        int age = Integer.parseInt(ageStr);//将年龄转换为int值

        //2将该用户信息保存
        /*
            将该用户以User对象形式序列化到users目录中，取名叫:[用户名].obj文件
         */
        User user = new User(username,password,nickname,age);
        File userFile = new File(userDir,username+".obj");
        try(
                FileOutputStream fos = new FileOutputStream(userFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
           oos.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //3设置response的正文文件为注册结果页面
    }
}







