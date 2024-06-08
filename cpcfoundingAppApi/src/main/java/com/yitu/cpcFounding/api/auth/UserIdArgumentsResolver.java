package com.yitu.cpcFounding.api.auth;

import com.yitu.cpcFounding.api.service.CommonService;
import com.yitu.cpcFounding.api.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @desc 
 * @author yaoyanhua
 * @date 2018/8/20 12:44
 * @param 
 * @return 
 */
@Component
public class UserIdArgumentsResolver implements HandlerMethodArgumentResolver {

    @Autowired
    private CommonService commonService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (webRequest instanceof ServletWebRequest) {
            final String userIdString = webRequest.getHeader("userId");
            final String token = webRequest.getHeader("token");
            if (StringUtils.isBlank(token) || StringUtils.isBlank(userIdString)) {
                return null;
            }
            final UserVO loginUser = commonService.getLoginUser(token);
            if (loginUser != null && String.valueOf(loginUser.getId()).equals(userIdString)) {
                return loginUser;
            }
            return null;
        }
        return null;
    }
}
