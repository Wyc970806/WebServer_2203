package com.webserver.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解用于标注那些处理某个特定请求的业务方法
 * DispatcherServlet只有扫描到某个被@Controller的类才会去扫描里面定义的所有方法，并且只有被@RequestMapping
 * 标注的方法才会判断是否是本次请求应当处理的业务方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String value();
}








