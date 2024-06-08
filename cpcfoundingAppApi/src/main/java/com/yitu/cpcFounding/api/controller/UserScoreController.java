package com.yitu.cpcFounding.api.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yitu.cpcFounding.api.auth.LoginRequired;
import com.yitu.cpcFounding.api.enums.SourceTypeEnum;
import com.yitu.cpcFounding.api.service.UserScoreService;
import com.yitu.cpcFounding.api.vo.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 积分控制器
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/9 15:08
 */

@LoginRequired
@Validated
@RestController
@RequestMapping("userScore")
@Api(value = "积分", tags = {"积分"})
public class UserScoreController {
    @Autowired
    private UserScoreService userScoreService;

    /**
     * 每日首次分享和签到加积分
     *
     * @param sourceType 积分来源类型
     * @return com.yitu.cpcFounding.api.vo.JsonResult
     * @author qixinyi
     * @date 2021/6/18 17:16
     */
    @PostMapping("addShareAndSignInScore")
    @ApiOperation(value = "每日首次分享和签到加积分", notes = "每日首次分享和签到加积分")
    @ApiImplicitParam(name = "sourceType", value = "积分来源类型 4每日首次分享 1签到", dataType = "int", required = true)
    @ApiOperationSupport(author = "祁心怡")
    public JsonResult addShareAndSignInScore(@RequestParam(name = "sourceType", defaultValue = "0") Integer sourceType) {
        return userScoreService.addShareAndSignInScore(sourceType);
    }
}
