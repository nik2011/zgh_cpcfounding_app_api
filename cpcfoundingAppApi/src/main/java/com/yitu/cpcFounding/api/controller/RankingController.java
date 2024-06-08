package com.yitu.cpcFounding.api.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yitu.cpcFounding.api.auth.LoginRequired;
import com.yitu.cpcFounding.api.service.TeamService;
import com.yitu.cpcFounding.api.service.UserScoreService;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.TeamScoreVO;
import com.yitu.cpcFounding.api.vo.UserScoreVO;
import com.yitu.cpcFounding.api.vo.userScore.UserScoreTotalVO;
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

/**
 * 排行榜控制器
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/7 17:00
 */

@LoginRequired
@Validated
@RestController
@RequestMapping("ranking")
@Api(value = "排行榜", tags = {"排行榜"})
public class RankingController {
    @Autowired
    private UserScoreService userScoreService;
    @Autowired
    private TeamService teamService;

    /**
     * 获取个人榜
     *
     * @param type      排行榜类型
     * @param pageSize  显示条数
     * @param pageIndex 页码
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.UserScoreVO>
     * @author qixinyi
     * @date 2021/6/8 15:50
     */
    @PostMapping("getUserRanking")
    @ApiOperation(value = "获取个人榜", notes = "获取个人榜")
    @ApiOperationSupport(author = "祁心怡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", required = true),
            @ApiImplicitParam(name = "type", value = "排行榜类型：1.周榜 2.总榜", dataType = "int", required = true)
    })
    public JsonResult<UserScoreVO> getUserRanking(@RequestParam(name = "type", defaultValue = "-1") Integer type, @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return userScoreService.findScoreSumByType(type, pageIndex, pageSize);
    }

    /**
     * 获取团队榜
     *
     * @param type      排行榜类型
     * @param pageSize  显示条数
     * @param pageIndex 页码
     * @param teamType  类型
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.TeamScoreVO>
     * @author qixinyi
     * @date 2021/6/16 18:00
     */
    @PostMapping("getTeamScoreRanking")
    @ApiOperation(value = "获取团队榜", notes = "获取团队榜")
    @ApiOperationSupport(author = "祁心怡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", required = true),
            @ApiImplicitParam(name = "type", value = "排行榜类型：1.周榜 2.总榜", dataType = "int", required = true),
            @ApiImplicitParam(name = "teamType", value = "类型 ：1.片区 2.街道 3.团队", dataType = "int", required = true)
    })
    public JsonResult<TeamScoreVO> getTeamScoreRanking(@RequestParam(name = "type", defaultValue = "-1") Integer type, @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "teamType", defaultValue = "0") Integer teamType) {
        return teamService.findScoreSumByType(type, pageIndex, pageSize, teamType);
    }

    /**
     * 获取答题榜
     *
     * @param pageSize  显示条数
     * @param pageIndex 页码
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.UserScoreVO>
     * @author qixinyi
     * @date 2021/6/9 10:34
     */
    @PostMapping("getAnswerScoreRanking")
    @ApiOperation(value = "获取答题榜", notes = "获取答题榜")
    @ApiOperationSupport(author = "祁心怡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", required = true)
    })
    public JsonResult<UserScoreVO> getAnswerScoreRanking(@RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return userScoreService.findAnswerScoreSum(pageIndex, pageSize);
    }

    /**
     * 获取用户积分
     *
     * @param
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.userScore.UserScoreTotalOfDayVO>
     * @author qixinyi
     * @date 2021/6/10 16:10
     */
    @PostMapping("getScoreTotalOfDay")
    @ApiOperation(value = "获取用户积分", notes = "获取用户积分")
    @ApiOperationSupport(author = "祁心怡")
    public JsonResult<UserScoreTotalVO> getScoreTotalOfDay() {
        return userScoreService.findScoreTotalOfDay();
    }

    /**
     * 获取片区街道下级排行榜
     *
     * @param pageIndex   页码
     * @param pageSize    页大小
     * @param parentId    片区/街道id
     * @param rankingType 排行榜类型
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.TeamScoreVO>
     * @author qixinyi
     * @date 2021/7/2 10:50
     */
    @PostMapping("getNextLevelRanking")
    @ApiOperation(value = "获取下级排行榜", notes = "获取下级排行榜")
    @ApiOperationSupport(author = "祁心怡")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", required = true),
            @ApiImplicitParam(name = "parentId", value = "片区/街道id", dataType = "long", required = true),
            @ApiImplicitParam(name = "rankingType", value = "排行榜类型：1.周榜 2.总榜", dataType = "int", required = true)
    })
    public JsonResult<TeamScoreVO> getNextLevelScoreRanking(@RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                            @RequestParam(value = "parentId", defaultValue = "0") Long parentId, @RequestParam(value = "rankingType", defaultValue = "0") Integer rankingType) {
        return teamService.findNextLevelScoreSum(pageIndex, pageSize, parentId, rankingType);
    }
}
