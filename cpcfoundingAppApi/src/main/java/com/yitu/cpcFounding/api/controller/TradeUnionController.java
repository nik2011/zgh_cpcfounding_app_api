package com.yitu.cpcFounding.api.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yitu.cpcFounding.api.service.TradeUnionService;
import com.yitu.cpcFounding.api.auth.LoginRequired;
import com.yitu.cpcFounding.api.vo.AuthSuccessDetailVo;
import com.yitu.cpcFounding.api.vo.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 工会认证接口
 *
 * @author jxc
 * @date 2021/1/23
 */
@RestController
@RequestMapping
@Api(value = "工会认证接口", tags = {"工会认证接口"})
public class TradeUnionController {

    @Resource
    private TradeUnionService tradeUnionService;

    /**
     * 工会认证111
     * @param phone 手机号
     * @return JsonResult<java.lang.Boolean>
     * @author jxc
     * @date 2021/1/23 15:57
     */
    @PostMapping("/trade/union/auth")
    @ApiOperation(value = "工会认证")
    @ApiOperationSupport(author = "江学成")
    @LoginRequired
    public JsonResult<Boolean> tradeUnionAuth(@ApiParam(value = "手机号",required = true) @RequestParam("phone") String phone,
                                              @ApiParam(value = "验证码",required = true) @RequestParam("code") String code) {
        return tradeUnionService.tradeUnionAuth(phone,code);
    }


    /**
     * 工会认证111
     * @param phone 手机号
     * @return JsonResult<java.lang.Boolean>
     * @author jxc
     * @date 2021/1/23 15:57
     */
    @GetMapping("/tradeUnionAuthNoCode")
    @ApiOperation(value = "工会认证")
    @ApiOperationSupport(author = "江学成")
    public JsonResult<Boolean> tradeUnionAuthNoCode(@ApiParam(value = "手机号",required = true) @RequestParam("phone") String phone,
                                                    @ApiParam(value = "key",required = true) @RequestParam("key") String key) {
        if(!"b9481a86e3897889d04291ad3f42c5ac".equals(key)){
            return JsonResult.fail("key异常");
        }
        return tradeUnionService.requestAuth(phone);
    }

    /**
     * 认证成功详情
     * @return JsonResult<java.lang.Boolean>
     * @author jxc
     * @date 2021/1/26 11:14
     */
    @PostMapping("/auth/success/detail")
    @ApiOperation(value = "认证成功详情")
    @ApiOperationSupport(author = "江学成")
    @LoginRequired
    public JsonResult<AuthSuccessDetailVo> authSuccessDetail() {
        return tradeUnionService.getAuthSuccessDetail();
    }



}
