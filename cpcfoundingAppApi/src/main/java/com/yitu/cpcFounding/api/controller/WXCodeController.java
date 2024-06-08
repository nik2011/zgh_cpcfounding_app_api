package com.yitu.cpcFounding.api.controller;

import javax.servlet.http.HttpServletRequest;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yitu.cpcFounding.api.auth.LoginUserUtil;
import com.yitu.cpcFounding.api.service.XCXCodeService;
import com.yitu.cpcFounding.api.utils.DateTimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.ad.WXCodeVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "小程序码", tags = "小程序码")
@RequestMapping("wxCode")
@Slf4j
public class WXCodeController {
    @Autowired
    private XCXCodeService xCXCodeService;
    @Autowired
    private LoginUserUtil loginUserUtil;

    /**
     * 获取小程序码以外的相关信息
     *
     * @param request
     * @param type     类型
     * @param flag     是否传用户id
     * @param detailId 详情id
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.ad.WXCodeVO>
     * @author qixinyi
     * @date 2021/6/18 15:41
     */
    @PostMapping("getCodeInfo")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型 22答题分享；23首页分享;73文章分享；74排行榜分享；75任务分享；76抽奖分享；77团队分享", dataType = "int", required = true),
            @ApiImplicitParam(name = "flag", value = "是否传用户id true传用户id;false传0"),
            @ApiImplicitParam(name = "detailId", value = "详情id", required = false)
    })
    @ApiOperation(value = "获取小程序码以外的相关信息")
    public JsonResult<WXCodeVO> getWXCodeInfo(HttpServletRequest request,
                                              @RequestParam(value = "type") Integer type,
                                              @RequestParam(value = "flag", required = false) Boolean flag,
                                              @RequestParam(value = "detailId", required = false, defaultValue = "0") Long detailId) {
        JsonResult<WXCodeVO> result = xCXCodeService.getWXCodeInfo(request, type, loginUserUtil.getUserId() + "", flag, detailId);
        return result;
    }

    /**
     * 获取小程序码
     *
     * @param page     参数
     * @param flag     是否传用户id
     * @param detailId 详情id
     * @return com.yitu.partyday.api.vo.JsonResult<java.lang.Object>
     * @author qixinyi
     * @date 2021/6/9 9:18
     */
    @PostMapping("getWXCode")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "跳转页面", dataType = "String", required = false),
            @ApiImplicitParam(name = "flag", value = "是否传用户id true:传用户id;false:传0"),
            @ApiImplicitParam(name = "detailId", value = "详情id", required = false)
    })
    @ApiOperationSupport(order = 1, author = "祁心怡")
    @ApiOperation(value = "获取小程序码")
    public JsonResult<Object> getWXCode(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "flag", required = false) Boolean flag,
            @RequestParam(value = "detailId", required = false) Long detailId) {
        return xCXCodeService.getCode(loginUserUtil.getUserId() + "", page, flag, detailId);
    }
}
