package com.webserver.controller;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

/**
 * 处理与用户相关操作的业务类
 */
public class UserController {
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

        //2将该用户信息保存

        //3设置response的正文文件为注册结果页面
    }
}








