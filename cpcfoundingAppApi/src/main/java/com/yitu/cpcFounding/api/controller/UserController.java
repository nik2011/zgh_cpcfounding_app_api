package com.yitu.cpcFounding.api.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yitu.cpcFounding.api.auth.LoginRequired;
import com.yitu.cpcFounding.api.service.UserScoreService;
import com.yitu.cpcFounding.api.service.UserService;
import com.yitu.cpcFounding.api.utils.DateTimeUtil;
import com.yitu.cpcFounding.api.vo.*;
import com.yitu.cpcFounding.api.vo.userScore.UserScoreTimesOfDay;
import com.yitu.cpcFounding.api.vo.userScore.UserScoresOfDay;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户控制器
 *
 * @author pangshihe
 * @date 2021/1/29 11:24
 */
@LoginRequired
@Validated
@RestController
@RequestMapping("user")
@Api(value = "用户控制器", tags = {"用户控制器"})
@Slf4j
public class UserController {
    @Resource
    private UserService userService;
    @Autowired
    private UserScoreService userScoreService;

    /**
     * 获取用户信息
     * @return JsonResult<com.yitu.women.api.dto.LoginUser>
     * @author shenjun
     * @date 2021/1/21 21:55
     */
    @PostMapping(value = "/getUserInfo")
    @ApiOperation(value = "获取用户信息")
    public JsonResult<UserVO> getUserInfo(){
        return userService.getUserInfo();
    }

    /**
     * 更新用户位置信息
     *
     * @param area 区域
     * @param lng  经度
     * @param lat  维度
     * @return JsonResult<java.lang.String>
     * @author pangshihe
     * @date 2021/1/29 11:51
     */
    @PostMapping(value = "updateUserLocation")
    @ApiOperation(value = "更新用户位置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "area", value = "区域", dataType = "String", required = true),
            @ApiImplicitParam(name = "lng", value = "经度", dataType = "String", required = true),
            @ApiImplicitParam(name = "lat", value = "维度", dataType = "String", required = true)
    })
    public JsonResult<String> updateUserLocation(@RequestParam(value = "area", defaultValue = "") @NotNull(message = "定位不能为空") String area,
                                                 @RequestParam(value = "lng", defaultValue = "") @NotNull(message = "经度不能为空") String lng,
                                                 @RequestParam(value = "lat", defaultValue = "") @NotNull(message = "维度不能为空") String lat) {
        return userService.updateUserLocation(area, lng, lat);
    }

    /**
     * 获取积分明细
     *
     * @param pageSize  显示条数
     * @param pageIndex 页码
     * @return com.yitu.cpcFounding.api.vo.JsonResult<java.util.List < com.yitu.cpcFounding.api.vo.UserScoreDetailVO>>
     * @author qixinyi
     * @date 2021/6/7 15:55
     */
    @PostMapping("getUserScoreDetails")
    @ApiOperation(value = "获取积分明细", notes = "获取积分明细")
    @ApiOperationSupport(author = "祁心怡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", required = true)
    })
    public JsonResult<List<UserScoreDetailVO>> getUserScoreDetails(@RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                                                                   @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return userScoreService.findUserScoreDetails(pageIndex, pageSize);
    }

    /**
     * 获取我的页面详情
     *
     * @param
     * @return com.yitu.cpcFounding.api.vo.JsonResult<UserDetailVO>
     * @author qixinyi
     * @date 2021/6/7 16:45
     */
    @PostMapping("getUserDetail")
    @ApiOperation(value = "获取我的页面详情", notes = "获取我的页面详情")
    @ApiOperationSupport(author = "祁心怡")
    public JsonResult<UserDetailVO> getUserDetail() {
        JsonResult<UserDetailVO> result = userService.findUserDetails();
        return result;
    }

    /**
     * 获取每日积分明细
     *
     * @param
     * @return com.yitu.cpcFounding.api.vo.JsonResult<UserScoresOfDay>
     * @author qixinyi
     * @date 2021/6/11 16:14
     */
    @PostMapping("getUserScoresOfDay")
    @ApiOperation(value = "获取每日各项任务积分", notes = "获取每日各项任务积分")
    @ApiOperationSupport(author = "祁心怡")
    public JsonResult<UserScoresOfDay> getUserScoresOfDay() {
        return userScoreService.findUserScoresOfDay();
    }

    /**
     * 刷新会员认证
     *
     * @param
     * @return com.yitu.cpcFounding.api.vo.JsonResult
     * @author shenjun
     * @date 2021-6-15 17:48:20
     */
    @PostMapping("refreshMember")
    @ApiOperation(value = "刷新会员认证", notes = "刷新会员认证")
    @ApiOperationSupport(author = "shenjun")
    public JsonResult refreshMember(){
        return userService.refreshMember();
    }

    /**
     * 获取每日次数明细
     *
     * @param
     * @return com.yitu.cpcFounding.api.vo.JsonResult<UserScoresOfDay>
     * @author qixinyi
     * @date 2021/6/11 16:14
     */
    @PostMapping("getUserScoreTimesOfDay")
    @ApiOperation(value = "获取每日各项任务次数", notes = "获取每日各项任务次数")
    @ApiOperationSupport(author = "祁心怡")
    public JsonResult<UserScoreTimesOfDay> getUserScoreTimesOfDay() {
        return userScoreService.findUserScoreTimesOfDay();
    }
}
