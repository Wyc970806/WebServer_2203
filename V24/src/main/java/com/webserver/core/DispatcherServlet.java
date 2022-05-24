package com.webserver.core;

import com.webserver.annotation.Controller;
import com.webserver.annotation.RequestMapping;
import com.webserver.controller.ArticleController;
import com.webserver.controller.ToolsController;
import com.webserver.controller.UserController;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.File;
import java.lang.reflect.Method;
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
            //获取com/webserver/controller目录下所有名字以.class结尾的文件(获取所有的字节码文件)
            File[] files = dir.listFiles(f->f.getName().endsWith(".class"));
            //遍历每一个字节码文件
            for(File file : files){
                //获取字节码文件名
                String fileName = file.getName();
                //根据字节码文件名获取类名
                String className = fileName.substring(0,fileName.indexOf("."));
                //加载该类的类对象
                Class cls = Class.forName("com.webserver.controller." + className);
                //判断该类是否被注解@Controller标注了
                if(cls.isAnnotationPresent(Controller.class)){
                    //获取该Controller中所有的方法
                    Method[] methods = cls.getDeclaredMethods();
                    //遍历每一个方法
                    for(Method method : methods){
                        //该方法是否被注解@RequestMapping标注了
                        if(method.isAnnotationPresent(RequestMapping.class)){
                            //获取该方法上的注解@RequestMapping
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);
                            //从注解上获取参数，用于匹配是否是当前请求
                            String value = rm.value();
                            //判断本次请求与该注解参数的请求路径是否一致，若一致则说明就是该方法处理这个请求
                            if(path.equals(value)){
                                //实例化该Controller
                                Object controller = cls.newInstance();
                                //执行该方法处理该业务
                                method.invoke(controller,request,response);
                                //处理业务完毕后直接return，避免执行下面的后续逻辑
                                return;
                            }
                        }
                    }
                }


            }


        } catch (Exception e) {
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
