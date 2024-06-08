package com.yitu.cpcFounding.api.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yitu.cpcFounding.api.service.PrizeWinnerService;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.ShowYearWinnerResponseVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 获奖用户表控制器
 * @author luzhichao
 * @date 2021-01-21 11:02:18
 */
@RestController("LsPrizeWinnerRestController")
@RequestMapping("/api/lsPrizeWinner")
@Api(value = "获奖用户表", tags = {"获奖用户表"})
public class PrizeWinnerController {

    @Autowired
    private PrizeWinnerService prizeWinnerService;

    /**
     * 获取晒年味获奖用户表列表数据（分页）
     * @param pageIndex 页索引
     * @param pageSize 页大小
     * @author luzhichao
     * @date 2021-01-21 11:02:18
     * @return 获奖用户表列表
     */
    @PostMapping("/getShowYearWinnerList")
    @ApiOperation(value = "获取获奖用户表列表数据（分页）")
    @ApiOperationSupport(author = "luzhichao")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", required = true)
    })
    public JsonResult<ShowYearWinnerResponseVo> getShowYearWinnerList(@RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                                                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        return prizeWinnerService.getShowYearWinnerList(pageIndex, pageSize);
    }




}
