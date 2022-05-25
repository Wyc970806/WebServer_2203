package com.webserver.core;

import com.webserver.annotation.Controller;
import com.webserver.annotation.RequestMapping;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 维护所有请求与处理该请求的Controller中业务方法的对应关系
 * 使得DispatcherServlet可以方便的获取请求对应的处理方法来进行反射调用
 */
public class HandlerMapping {
    /*
        key:请求路径
        value:处理该请求的Controller及其对应的业务方法
     */
    private static Map<String,MethodMapping> map = new HashMap<>();

    static {
        initMapping();
    }

    private static void initMapping(){
        try {
            File dir = new File(
                    HandlerMapping.class.getClassLoader()
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
                    //实例化Controller实例
                    Object controller = cls.newInstance();
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
                            //将该业务方法以及该方法所属的Controller实例以一个MethodMapping对象表示
                            MethodMapping methodMapping = new MethodMapping(controller,method);
                            /*
                                key:该方法对应的请求路径(从方法上的注解@RequestMapping中获取的参数)
                                value:MethodMapping对象:处理该请求的方法及方法所属的Controller实例
                             */
                            map.put(value,methodMapping);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        System.out.println(map);
        MethodMapping mm = map.get("/myweb/showAllUser");
        System.out.println(mm.getController());
        System.out.println(mm.getMethod().getName());
    }


    public static class MethodMapping{
        private Object controller;
        private Method method;

        public MethodMapping(Object controller, Method method) {
            this.controller = controller;
            this.method = method;
        }

        public Object getController() {
            return controller;
        }

        public void setController(Object controller) {
            this.controller = controller;
        }

        public Method getMethod() {
            return method;
        }

        public void setMethod(Method method) {
            this.method = method;
        }
    }
}
