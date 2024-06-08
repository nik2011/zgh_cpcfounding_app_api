package com.yitu.cpcFounding.api.interceptors;

import com.alibaba.fastjson.JSON;
import com.yitu.cpcFounding.api.constant.Configs;
import com.yitu.cpcFounding.api.auth.LoginUserUtil;
import com.yitu.cpcFounding.api.enums.ResponseCode;
import com.yitu.cpcFounding.api.inteface.AccessLimit;
import com.yitu.cpcFounding.api.redis.RedisUtil;
import com.yitu.cpcFounding.api.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.annotation.Annotation;

/**
 * @author liuzhaowei
 * @date 2021/2/18
 */
@Component
public class AntiBrushIntercept extends HandlerInterceptorAdapter {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LoginUserUtil loginUserUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        AccessLimit accessLimit = findAnnotation(handlerMethod, AccessLimit.class);
        if (accessLimit == null) {
            return true;
        }
        int seconds = accessLimit.seconds();
        int maxCount = accessLimit.maxCount();
        boolean needLogin = accessLimit.needLogin();
        String key = Configs.REDIS_ACCESS_API_LIMIT + request.getRequestURI();
        if (needLogin) {
            long userId = loginUserUtil.getUserId();
            key += "_" + userId;//获取用户id
        }
        Object accessObject = redisUtil.get(key);
        if (accessObject == null) {
            redisUtil.set(key, 1, seconds);
            return true;
        }
        int accessCount = accessObject != null ? (int) accessObject : 0;
        if (accessCount < maxCount) {
            redisUtil.incr(key, 1);
            return true;
        } else {
            render(response, ResponseCode.FORBIDDEN);
            return false;
        }
    }

    private void render(HttpServletResponse response, ResponseCode responseCode) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(JsonResult.fail(responseCode.getMessage()));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    private <T extends Annotation> T findAnnotation(HandlerMethod handler, Class<T> annotationType) {
        T annotation = handler.getBeanType().getAnnotation(annotationType);
        if (annotation != null) return annotation;
        return handler.getMethodAnnotation(annotationType);
    }
}
