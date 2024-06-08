package com.yitu.cpcFounding.api.auth;

import java.lang.annotation.*;

/**
 * @desc 
 * @author yaoyanhua
 * @date 2018/8/20
 * @param 
 * @return 
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginUser {
}
