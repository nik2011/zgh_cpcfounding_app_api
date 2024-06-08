package com.yitu.cpcFounding.api.controller.prize;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yitu.cpcFounding.api.auth.LoginRequired;
import com.yitu.cpcFounding.api.dto.userprize.UserPrizeDetailDTO;
import com.yitu.cpcFounding.api.service.WxVoucherService;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.userprize.UserPrizeDetailVO;
import com.yitu.cpcFounding.api.vo.userprize.UserPrizeListVO;
import com.yitu.cpcFounding.api.vo.userprize.UserPrizeVO;
import com.yitu.cpcFounding.api.service.UserPrizeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户奖品表控制器
 *
 * @author shenjun
 * @date 2021-01-21 11:47:39
 */
@RestController("LsUserPrizeRestController")
@RequestMapping("/api/lsUserPrize")
@Api(value = "用户奖品模块", tags = {"用户奖品模块"})
@Validated
public class UserPrizeController {

    @Autowired
    private UserPrizeService userPrizeService;

    @Autowired
    private WxVoucherService wxVoucherService;


    @ApiOperation(value = "我的奖品-我的奖品列表", notes = "我的奖品列表")
    @ApiOperationSupport(author = "陈品佳")
    @ApiImplicitParam(name = "userId", value = "用户id", dataType = "string")
    @PostMapping("/list")
    @LoginRequired
    public JsonResult<List<UserPrizeListVO>> getUserPrizeList() {
        return userPrizeService.getUserPrizeList();
    }

    @ApiOperation(value = "我的奖品-获取奖品信息", notes = "获取奖品信息")
    @ApiOperationSupport(author = "陈品佳")
    @ApiImplicitParam(name = "id", value = "用户奖品记录id", dataType = "string", required = true)
    @PostMapping("/detail")
    @LoginRequired
    public JsonResult<UserPrizeDetailVO> getUserPrizeDetail(@RequestParam("id") @NotNull(message = "用户奖品记录id不能为空") Long id) {
        return userPrizeService.getUserPrizeDetail(id);
    }

    @ApiOperation(value = "我的奖品-保存领奖信息", notes = "保存领奖信息")
    @ApiOperationSupport(author = "陈品佳")
    @PostMapping("/save")
    @LoginRequired
    public JsonResult saveUserPrizeInfo(@RequestBody @Valid UserPrizeDetailDTO saveInfo) {
        return userPrizeService.saveUserPrizeDetailInfo(saveInfo);
    }

    @ApiOperation(value = "我的奖品-领取代金券", notes = "领取代金券")
    @ApiOperationSupport(author = "陈品佳")
    @ApiImplicitParam(name = "id", value = "用户奖品记录id", dataType = "string", required = true)
    @PostMapping("/takeVxVoucher")
    @LoginRequired
    public JsonResult takeVxVoucher(@RequestParam("id") @NotNull(message = "用户奖品记录id不能为空") Long id) {
        return wxVoucherService.sendVoucher(id);
    }

    /**
     * 摇一摇首页中奖列表接口
     *
     * @return JsonResult<java.util.List < UserPrizeVO>>
     * @author qinmingtong
     * @date 2021/1/21 18:19
     */
    @ApiOperation(value = "摇一摇首页中奖列表接口", notes = "摇一摇首页中奖列表接口")
    @ApiOperationSupport(author = "秦明通")
    @PostMapping("/userPrizeOfYaoYiYao")
    public JsonResult<List<UserPrizeVO>> userPrizeOfYaoYiYao() {
        return userPrizeService.userPrizeOfYaoYiYao();
    }

    /**
     * 小程序首页中奖列表接口
     *
     * @param activeType 活动类型
     * @param count      需要查询的记录数
     * @return JsonResult<java.util.List < UserPrizeVO>>
     * @author qinmingtong
     * @date 2021/1/22 16:06
     */
    @ApiOperation(value = "小程序首页中奖列表接口", notes = "小程序首页中奖列表接口")
    @ApiOperationSupport(author = "秦明通")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activeType", dataType = "int",
                    value = "活动类型（默认不需要传） 1.摇一摇活动 2.晒年味 3.暖城活动"),
            @ApiImplicitParam(name = "count", dataType = "int", value = "需要查询的记录数，默认查询前10条，最大不超过100")
    })
    @PostMapping("/userPrizeList")
    public JsonResult<List<UserPrizeVO>> userPrizeList(
            @RequestParam(value = "activeType", required = false) Integer activeType,
            @RequestParam(value = "count", defaultValue = "10")
            @Max(value = 100, message = "查询记录数不能超过100") Integer count) {
        return userPrizeService.userPrizeList(activeType, count);
    }

    /**
     * 中奖列表分页查询接口
     *
     * @param pageIndex  当前页
     * @param pageSize   每页大小
     * @param activeType 活动类型
     * @return JsonResult<java.util.List < UserPrizeVO>>
     * @author qinmingtong
     * @date 2021/1/22 16:06
     */
    @ApiOperation(value = "中奖列表分页查询接口", notes = "中奖列表分页查询接口")
    @ApiOperationSupport(author = "秦明通")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", required = true),
            @ApiImplicitParam(name = "activeType", dataType = "int", value = "活动类型：1大转盘 2送奖章")
    })
    @PostMapping("/userPrizePageList")
    public JsonResult<Page<UserPrizeVO>> userPrizePageList(
            @RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
            @RequestParam(value = "activeType", required = false) Integer activeType) {
        return JsonResult.ok(userPrizeService.userPrizePageList(pageIndex, pageSize, activeType));
    }

}
