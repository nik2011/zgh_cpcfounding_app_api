package com.yitu.cpcFounding.api.controller.team;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yitu.cpcFounding.api.auth.LoginRequired;
import com.yitu.cpcFounding.api.service.TeamService;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.team.TeamUserListVO;
import com.yitu.cpcFounding.api.vo.userScore.UserListVO;
import com.yitu.cpcFounding.api.vo.team.UserTeamListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * 用户团队控制器
 * Project Name: cpcFoundingAppApi
 * File Name: UserMedalController
 * author: wangping
 * Date: 2021/4/9 11:50
 */
@Api(value = "团队模块", tags = "团队模块")
@RestController
@RequestMapping("/api/team")
public class TeamController {

    @Autowired
    private TeamService teamService;

    /**
     * 获取团队列表数据（分页）
     * @param name 团队名称
     * @param pageIndex 页索引
     * @param pageSize 页大小
     * @author wangping
     * @date 2021-04-09
     * @return 获取团队列表
     */
    @PostMapping("/getTeamList")
    @ApiOperation(value = "团队列表（分页）")
    @ApiOperationSupport(author = "王平")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", required = true),
            @ApiImplicitParam(name = "name", value = "团队名称", dataType = "String"),
            @ApiImplicitParam(name = "type", value = "1 片区 2 街道 3 团队", dataType = "Integer")
    })
    public JsonResult<UserTeamListVO> getTeamList(@RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                                                  @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                  @RequestParam(value = "type",required = false) Integer type,
                                                  @RequestParam(value = "name",required = false) String name) {

        return teamService.getTeamList(name,pageIndex, pageSize,type);
    }



    @ApiOperation(value = "加入团队", notes = "加入团队")
    @ApiOperationSupport(author = "王平")
    @ApiImplicitParam(name = "teamId", value = "团队id", dataType = "int", required = true)
    @PostMapping("/userAddTeam")
    @LoginRequired
    public JsonResult userAddTeam(@RequestParam("teamId") @NotNull(message = "团队id不能为空") int teamId){
        return teamService.userAddTeam(teamId);
    }

    /**
     *
     * 获取团队信息详情以及团队下的用户所拥有的积分排行（分页）
     * @param teamId
     * @return com.yitu.mayday.api.vo.JsonResult<com.yitu.mayday.api.vo.userMedal.TeamUserListVO>
     * @author wangping
     * @date 2021/4/9 14:44
     */
    @ApiOperation(value = "团队信息详情，团队用户积分排行", notes = "团队信息详情，团队用户积分排行")
    @ApiOperationSupport(author = "王平")
    @ApiImplicitParam(name = "teamId", value = "团队id", dataType = "int", required = true)
    @PostMapping("/teamDetail")
    public JsonResult<TeamUserListVO> teamDetail(@RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                                             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                             @RequestParam("teamId") @NotNull(message = "团队id不能为空") int teamId){
        return teamService.getTeamById(teamId,pageIndex,pageSize);
    }

}
