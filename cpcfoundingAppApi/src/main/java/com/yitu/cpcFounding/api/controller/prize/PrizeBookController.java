package com.yitu.cpcFounding.api.controller.prize;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yitu.cpcFounding.api.vo.prizeBook.PrizeBookVO;
import com.yitu.cpcFounding.api.auth.LoginRequired;
import com.yitu.cpcFounding.api.service.PrizeBookService;
import com.yitu.cpcFounding.api.vo.prizeBook.ActiveTimeVo;
import com.yitu.cpcFounding.api.enums.ActiveEntranceEnum;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.redis.YaoPrizeBookVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 奖品库表控制器
 *
 * @author shenjun
 * @date 2021-01-21 11:47:32
 */
@RestController("LsPrizeBookRestController")
@RequestMapping("/api/lsPrizeBook")
@Api(value = "奖品库模块", tags = {"奖品库模块"})
public class PrizeBookController {

    @Autowired
    private PrizeBookService prizeBookService;

    /**
     * @param
     * @return JsonResult
     * @Description: 获取摇一摇数量
     * @author liuzhaowei
     * @date 2021/1/21
     */
    @PostMapping("getYaoYiYaoCount")
    @ApiOperation(value = "获取摇一摇数量")
    @ApiOperationSupport(author = "刘兆伟")
    @LoginRequired
    public JsonResult getYaoYiYaoCount() {
       // return lsPrizeBookService.getYaoYiYaoCount();
        return prizeBookService.getYaoYiYaoCountRedis();
    }

    /**
     * @param
     * @return JsonResult
     * @Description: 执行摇一摇
     * @author liuzhaowei
     * @date 2021/1/21
     */
    @PostMapping("doYaoYiYao")
    @ApiOperation(value = "执行摇一摇")
    @ApiOperationSupport(author = "刘兆伟")
    @LoginRequired
    //@AccessLimit
    public JsonResult<YaoPrizeBookVo> doShake() {
       // return lsPrizeBookService.doShake();
        return prizeBookService.doShakeRedis();
    }

    /**
     * 摇一摇首页奖品列表接口
     *
     * @return JsonResult<java.util.List < PrizeBookVO>>
     * @author qinmingtong
     * @date 2021/1/21 17:30
     */
    @ApiOperation(value = "摇一摇首页奖品列表接口", notes = "摇一摇首页奖品列表接口")
    @ApiOperationSupport(author = "秦明通")
    @PostMapping("/prizeBookOfYaoYiYao")
    public JsonResult<List<PrizeBookVO>> prizeBookOfYaoYiYao() {
        return prizeBookService.prizeBookList(ActiveEntranceEnum.SHAKE.getValue());
    }

    /**
     * 根据活动类型查询奖品列表
     *
     * @param activeType 活动类型
     * @return JsonResult<java.util.List<PrizeBookVO>>
     * @author qinmingtong
     * @date 2021/1/23 8:51
     */
    @ApiOperation(value = "根据活动类型查询奖品列表", notes = "根据活动类型查询奖品列表")
    @ApiOperationSupport(author = "秦明通")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activeType", dataType = "int",
                    value = "活动类型（默认不需要传） 1.摇一摇活动 2.晒年味 3.暖城活动")
    })
    @PostMapping("/prizeBookList")
    public JsonResult<List<PrizeBookVO>> prizeBookList(@RequestParam(value = "activeType",
            required = false)Integer activeType) {
        return prizeBookService.prizeBookList(activeType);
    }

    /**
     * @param
     * @return JsonResult
     * @Description: 获取活动开始结束时间
     * @author liuzhaowei
     * @date 2021/1/21
     */
    @PostMapping("getActiveTime")
    @ApiOperation(value = "获取活动开始结束时间")
    @ApiOperationSupport(author = "刘兆伟")
    public JsonResult<ActiveTimeVo> getActiveTime(@ApiParam(value = "活动类型 1 摇一摇活动 2 晒年味 3 暖城活动 4 答题活动 5 送奖章")
                                                  @RequestParam(value = "activeType") int activeType) {
        return prizeBookService.getActiveTime(activeType);
    }


}
