package com.yitu.cpcFounding.api.controller;

import com.yitu.cpcFounding.api.auth.LoginRequired;
import com.yitu.cpcFounding.api.service.ShareRecordService;
import com.yitu.cpcFounding.api.vo.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Auther: zhangyongfeng
 * @Date: 2021/1/22 14 : 12
 * @Description:
 */
@Api(value = "分享记录", tags = "分享记录")
@RequestMapping("shareLog")
@RestController
@LoginRequired
public class ShareRecordController {
    @Resource
    ShareRecordService shareRecordService;

    /**
     * 添加用户分享记录信息
     *
     * @param type
     * @return JsonResult<java.lang.String>
     * @author zhangyongfeng
     * @date 2021/1/22 14:21
     */
    @ApiOperation(value = "添加用户分享记录信息", notes = "添加用户分享记录信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "分享类型：1.摇一摇活动 2.晒年味 3.暖城活动", dataType = "int", required = true)
    })
    @PostMapping("addShareRecord")
    public JsonResult<String> addShareRecord(@RequestParam(value = "type") int type) {
        return shareRecordService.addShareRecord(type);
    }
}
