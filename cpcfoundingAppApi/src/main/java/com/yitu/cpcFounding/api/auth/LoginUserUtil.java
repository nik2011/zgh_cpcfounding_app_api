package com.yitu.cpcFounding.api.auth;

import com.yitu.cpcFounding.api.service.CommonService;
import com.yitu.cpcFounding.api.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Class
 *
 * @author yaoyanhua
 * @version 1.0
 * @date 2021/1/21
 */
@Component
public class LoginUserUtil {

    @Autowired
    private CommonService commonService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 获取登录信息
     * @return com.yitu.women.api.dto.LoginUser
     * @author yaoyanhua
     * @date 2021/1/21 23:02
     */
    public UserVO getLoginUser() {
        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)) {
//            UserVO userVO = new UserVO();
//            userVO.setId(1L);
//            userVO.setWxUserName("子皮");
//            return userVO;
            return null;
        }
        return commonService.getLoginUser(token);
    }

    /**
     * 获取用户id
     */
    public long getUserId(){
        UserVO loginUser = getLoginUser();
        if(null == loginUser){
            return 0;
        }
        return loginUser.getId();
    }

    /**
     * 获取用户名称
     */
    public String getUserName(){
        UserVO loginUser = getLoginUser();
        if(null == loginUser){
            return "";
        }
        return loginUser.getWxUserName();
    }

}
