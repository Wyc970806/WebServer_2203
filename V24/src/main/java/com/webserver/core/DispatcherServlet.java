package com.webserver.core;

import com.webserver.controller.ArticleController;
import com.webserver.controller.ToolsController;
import com.webserver.controller.UserController;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;

/**
 * 处理请求的类
 */
public class DispatcherServlet {
    private static File rootDir;
    public static File staticDir;
    static {
        try {
            rootDir = new File(
                    DispatcherServlet.class.getClassLoader()
                            .getResource(".").toURI()
            );
            staticDir = new File(rootDir,"static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response){
        String path = request.getRequestURI();
        //首先根据请求路径判断是否为请求某个业务处理
        if("/myweb/reg".equals(path)){
            System.out.println("开始处理用户注册!!!!!!!!!!!!!!!!!!!!");
            UserController controller = new UserController();
            controller.reg(request,response);

        }else if("/myweb/login".equals(path)){
            UserController controller = new UserController();
            controller.login(request,response);

        }else if("/myweb/writeArticle".equals(path)){
            ArticleController controller = new ArticleController();
            controller.writeArticle(request,response);

        }else if("/myweb/showAllUser".equals(path)){
            UserController controller = new UserController();
            controller.showAllUser(request,response);


        }else if("/myweb/createQR".equals(path)){
            ToolsController controller = new ToolsController();
            controller.createQR(request,response);
        }else {
            File file = new File(staticDir, path);   // path:/myweb/index.html
            if (file.isFile()) {//file表示的是否为一个文件
                response.setContentFile(file);

            } else {//file表示的是一个目录或file表示的路径并不存在
                response.setStatusCode(404);
                response.setStatusReason("NotFound");
                file = new File(staticDir, "/root/404.html");
                response.setContentFile(file);
            }
        }

        response.addHeader("Server","WebServer");
    }
}
