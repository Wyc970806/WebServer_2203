package com.webserver.controller;

import com.webserver.entity.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
     * 处理显示所有用户列表的动态页面
     * @param request
     * @param response
     */
    public void showAllUser(HttpServletRequest request,HttpServletResponse response){
        //1将users目录下的所有obj文件进行反序列化，得到所有的注册用户
        List<User> userList = new ArrayList<>();
        //1.1获取users目录下的所有obj文件(文件过滤器)
        //1.2遍历每一个obj文件并利用对象输入流进行反序列化
        //1.3将反序列化的对象存入userList备用
        File[] users = userDir.listFiles(f->f.getName().endsWith(".obj"));
        for(File userFile : users){
            try (
                    ObjectInputStream ois = new ObjectInputStream(
                            new FileInputStream(
                                    userFile
                            )
                    )
            ){
               User user = (User)ois.readObject();
               userList.add(user);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        /*
            2生成一个HTML页面(动态页面)并包含所有用户数据
            动态页面:每次访问该页面时，页面都是由程序临时生成的。
            静态页面:页面是事先准备好的，写死在文件里的。

            动态资源:现用现生成的资源
            静态资源:事先准备好，写死在文件里的。
         */
        File file = new File("./users.html");//生成的页面文件
        try {
            PrintWriter pw = new PrintWriter(file);
            pw.println("<!DOCTYPE html>");
            pw.println("<html lang=\"en\">");
            pw.println("<head>");
            pw.println("<meta charset=\"UTF-8\">");
            pw.println("<title>用户列表</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("<center>");
            pw.println("<h1>用户列表</h1>");
            pw.println("<table border=\"1\">");
            pw.println("<tr>");
            pw.println("<td>用户名</td>");
            pw.println("<td>密码</td>");
            pw.println("<td>昵称</td>");
            pw.println("<td>年龄</td>");
            pw.println("</tr>");
            for(User user : userList) {
                pw.println("<tr>");
                pw.println("<td>"+ user.getUsername()+"</td>");
                pw.println("<td>"+ user.getPassword()+"</td>");
                pw.println("<td>"+ user.getNickname()+"</td>");
                pw.println("<td>"+ user.getAge()+"</td>");
                pw.println("</tr>");
            }
            pw.println("</table>");
            pw.println("</center>");
            pw.println("</body>");
            pw.println("</html>");
            pw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //3将生成的动态页面通过响应对象发送给浏览器
        response.setContentFile(file);

    }

    /**
     * 处理用户登录业务
     * @param request
     * @param response
     */
    public void login(HttpServletRequest request,HttpServletResponse response){
        //1通过request获取表单信息
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(username.isEmpty()||password.isEmpty()){//如果没有输入内容则提示输入信息有误
            response.sendRedirect("/myweb/login_info_error.html");
            return;
        }

        //2处理登录
        File userFile = new File(userDir,username+".obj");
        if(userFile.exists()){//该文件存在，才说明用户名输入对了
            //将该用户注册信息读取回来，用于比对密码
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userFile));){
                User user = (User)ois.readObject();
                if(user.getPassword().equals(password)){//密码正确，登录成功
                    response.sendRedirect("/myweb/login_success.html");
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //只要执行到这里，就说明登录失败了1:文件不存在(用户名不对) 2:密码不对
        response.sendRedirect("/myweb/login_fail.html");
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

        if(username.isEmpty()||password.isEmpty()||nickname.isEmpty()||ageStr.isEmpty()||
            !ageStr.matches("[0-9]+")){
            //响应一个错误页面给用户
            response.sendRedirect("/myweb/reg_info_error.html");
            return;
        }



        int age = Integer.parseInt(ageStr);//将年龄转换为int值

        //2将该用户信息保存
        /*
            将该用户以User对象形式序列化到users目录中，取名叫:[用户名].obj文件
         */
        User user = new User(username,password,nickname,age);
        File userFile = new File(userDir,username+".obj");

        //判断该用户是否已经存在，若存在则响应页面:/myweb/have_user.html  页面居中一行字:该用户已存在，请重新注册
        if(userFile.exists()){
            response.sendRedirect("/myweb/have_user.html");
            return;
        }



        try(
                FileOutputStream fos = new FileOutputStream(userFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
           oos.writeObject(user);

           response.sendRedirect("/myweb/reg_success.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //3设置response的正文文件为注册结果页面

    }
}








