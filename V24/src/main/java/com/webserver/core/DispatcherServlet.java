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
            staticDir = new File(rootDir, "static");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void service(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getRequestURI();
        //首先根据请求路径判断是否为请求某个业务处理
        /*
            1:扫描com/webserver/controller目录，根据该目录中所有的字节码文件的文件名来确定类名
              从而得知在com.webserver.controller包下所有类的类名了。
            2:使用Class.forName加载com.webserver.controller包下对应的类
            3:判断该类是否有被注解@Controller标注，只有被标注的类才是我们任何的处理业务的类
            4:获取该类中定义的所有方法，并判断每个方法是否有被@RequestMapping标注。
              并且还要判断@RequestMapping中的参数值是否与本次请求路径path的值是否相同，若相同则说明
              该方法就是处理本次请求的方法，从而我们实例化该Controller并调用该方法。调用完毕后直接return
              将当前DispatcherServlet的service方法结束(目的是不走下面的分支了)
         */
        try {
            File dir = new File(
                    DispatcherServlet.class.getClassLoader()
                            .getResource("./com/webserver/controller").toURI()
            );

            Class.forName("com.webserver.controller."+类名);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


        File file = new File(staticDir, path);   // path:/myweb/index.html
        if (file.isFile()) {//file表示的是否为一个文件
            response.setContentFile(file);

        } else {//file表示的是一个目录或file表示的路径并不存在
            response.setStatusCode(404);
            response.setStatusReason("NotFound");
            file = new File(staticDir, "/root/404.html");
            response.setContentFile(file);
        }
        response.addHeader("Server", "WebServer");
    }
}
