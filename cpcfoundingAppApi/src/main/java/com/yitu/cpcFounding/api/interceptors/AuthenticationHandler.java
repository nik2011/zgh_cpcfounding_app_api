package com.yitu.cpcFounding.api.interceptors;

import com.yitu.cpcFounding.api.auth.LoginRequired;
import com.yitu.cpcFounding.api.enums.ResponseCode;
import com.yitu.cpcFounding.api.exception.ConsciousException;
import com.yitu.cpcFounding.api.service.CommonService;
import com.yitu.cpcFounding.api.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;

/**
 * @desc
 * @author yanghaoming
 * @date 2018/8/20 13:31
 * @param
 * @return
 */
@Component
public class AuthenticationHandler implements HandlerInterceptor {

    @Autowired
    private CommonService commonService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod) {
            final LoginRequired loginRequired = findAnnotation((HandlerMethod) handler, LoginRequired.class);
            if (loginRequired == null) {
                return true;
            }
            final String userIdStr = request.getHeader("userId");
            final String token = request.getHeader("token");
            if (StringUtils.isBlank(userIdStr) || StringUtils.isBlank(token)) {
                throw new ConsciousException(ResponseCode.UNAUTHORIZED);
            }
            try {
                final UserVO loginUser = commonService.getLoginUser(token);
                if(loginUser == null) {
                    throw new ConsciousException(ResponseCode.UNAUTHORIZED);
                }
                if (!String.valueOf(loginUser.getId()).equals(userIdStr)) {
                    throw new ConsciousException(ResponseCode.UNAUTHORIZED);
                }
                return true;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            throw new ConsciousException(ResponseCode.UNAUTHORIZED);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

    private <T extends Annotation> T findAnnotation(HandlerMethod handler, Class<T> annotationType) {
        T annotation = handler.getBeanType().getAnnotation(annotationType);
        if (annotation != null) return annotation;
        return handler.getMethodAnnotation(annotationType);
    }
}
