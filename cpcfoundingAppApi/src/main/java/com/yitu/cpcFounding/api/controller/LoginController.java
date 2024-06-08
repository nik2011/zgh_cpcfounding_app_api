package com.yitu.cpcFounding.api.controller;

import com.yitu.cpcFounding.api.auth.LoginRequired;
import com.yitu.cpcFounding.api.auth.LoginUser;
import com.yitu.cpcFounding.api.auth.LoginUserUtil;
import com.yitu.cpcFounding.api.service.UserService;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录
 *
 * @author yaoyanhua
 * @version 1.0
 * @date 2021/1/21
 */
@RestController
@RequestMapping("/login")
@Api(value = "登录", tags = {"登录"})
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 微信授权登录
     * @param code
     * @param avatarUrl
     * @param nickName
     * @param request
     * @return JsonResult<com.yitu.women.api.dto.LoginUser>
     * @author yaoyanhua
     * @date 2021/1/21 21:55
     */
    @PostMapping(value = "/loginByWeChat")
    @ApiOperation(value = "微信授权登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "微信临时登录码", dataType = "String", required = true),
            @ApiImplicitParam(name = "avatarUrl", value = "微信头像", dataType = "String", required = true),
            @ApiImplicitParam(name = "nickName", value = "微信昵称", dataType = "String", required = true)
    })
    public JsonResult<UserVO> loginByWeChat(@RequestParam(value = "code", defaultValue = "") String code,
                                            @RequestParam(value = "avatarUrl", defaultValue = "") String avatarUrl,
                                            @RequestParam(value = "nickName", defaultValue = "") String nickName,
                                            HttpServletRequest request) throws IOException {
        return userService.loginByWeChat(code, avatarUrl, nickName, request);
    }

    /**
     * 获取用户手机号通过微信授权,并且认证
     * @param code
     * @param encryptedData
     * @param iv
     * @param request
     * @return JsonResult<com.yitu.women.api.dto.LoginUser>
     * @author yaoyanhua
     * @date 2021/1/21 21:55
     */
    @PostMapping(value = "/getPhoneAndAuthByWxAuth")
    @ApiOperation(value = "获取用户手机号通过微信授权,并且认证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "微信临时登录码", dataType = "String", required = true),
            @ApiImplicitParam(name = "encryptedData", value = "微信加密数据", dataType = "String", required = true),
            @ApiImplicitParam(name = "iv", value = "偏移量", dataType = "String", required = true)
    })
    public JsonResult<String> getPhoneAndAuthByWxAuth(@RequestParam(value = "code", defaultValue = "") String code,
                                               @RequestParam(value = "encryptedData", defaultValue = "") String encryptedData,
                                               @RequestParam(value = "iv", defaultValue = "") String iv,
                                               HttpServletRequest request) throws IOException {
        return userService.getPhoneAndAuthByWxAuth(code, encryptedData, iv, request);
    }

    /**
     * 获取用户手机号通过微信授权
     * @param code
     * @param encryptedData
     * @param iv
     * @param request
     * @return JsonResult<com.yitu.women.api.dto.LoginUser>
     * @author yaoyanhua
     * @date 2021/1/21 21:55
     */
    @PostMapping(value = "/getPhoneByWxAuth")
    @ApiOperation(value = "获取用户手机号通过微信授权")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "微信临时登录码", dataType = "String", required = true),
            @ApiImplicitParam(name = "encryptedData", value = "微信加密数据", dataType = "String", required = true),
            @ApiImplicitParam(name = "iv", value = "偏移量", dataType = "String", required = true)
    })
    public JsonResult<String> getPhoneByWxAuth(@RequestParam(value = "code", defaultValue = "") String code,
                                               @RequestParam(value = "encryptedData", defaultValue = "") String encryptedData,
                                               @RequestParam(value = "iv", defaultValue = "") String iv,
                                               HttpServletRequest request) throws IOException {
        return userService.getPhoneByWxAuth(code, encryptedData, iv, request);
    }



    @PostMapping(value = "/jscode2sessionTest")
    @ApiIgnore
    public Map jscode2sessionTest(@RequestParam(value = "js_code", defaultValue = "") String code) {
        Map codeTest = new HashMap();
        codeTest.put("openid", code);
        return codeTest;
    }

    @PostMapping(value = "/getUserTest")
    @LoginRequired
    @ApiIgnore
    public JsonResult<UserVO> getUserTest(@LoginUser UserVO user) {
        return JsonResult.ok(user);
    }
}
