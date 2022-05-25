package com.webserver.core;

import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

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
        HandlerMapping.MethodMapping mm = HandlerMapping.getMethod(path);
        if (mm != null) {//如果根据请求路径提取到对应的处理方法，说明该请求是请求业务
            Object controller = mm.getController();
            Method method = mm.getMethod();
            try {
                method.invoke(controller,request,response);
            } catch (Exception e) {
                e.printStackTrace();
                //当调用某个网络应用中的处理业务方法时，该方法若抛出异常，这里应当响应500错误给浏览器
                //参照下方404的方式，设置即可
            }
        } else {
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
        response.addHeader("Server", "WebServer");
    }
}
