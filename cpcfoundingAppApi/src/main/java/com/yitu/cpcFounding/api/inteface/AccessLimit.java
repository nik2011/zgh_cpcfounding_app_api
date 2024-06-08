package com.yitu.cpcFounding.api.inteface;

import java.lang.annotation.*;

/**
 * @author liuzhaowei
 * 防刷注解
 * @date 2021/2/18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface AccessLimit {
    //间隔秒数
    int seconds() default 1;

    //最大次数
    int maxCount() default 1;

    //是否需要登录
    boolean needLogin() default true;
}
