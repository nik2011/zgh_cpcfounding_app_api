package com.yitu.cpcFounding.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yitu.cpcFounding.api.domain.Team;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.TeamScoreVO;
import com.yitu.cpcFounding.api.vo.team.TeamUserListVO;
import com.yitu.cpcFounding.api.vo.userScore.UserListVO;
import com.yitu.cpcFounding.api.vo.team.UserTeamListVO;

/**
 * 团队服务
 *
 * @author wangping
 * @date 2021-04-09 11:47:32
 */
public interface TeamService {


    /**
     * 获取团队列表以及当前用户所属团队数据（分页）
     *
     * @param name      团队名称
     * @param pageIndex 页索引
     * @param pageSize  页大小
     * @return 获奖团队表列表实体
     * @author wangping
     * @date 2021-04-09 15:02:18
     */
    JsonResult<UserTeamListVO> getTeamList(String name, int pageIndex, int pageSize,Integer type);

    /**
     * 获取个人列表以及当前用户所属数据，根据奖章排序，个人榜（分页）
     * 只查询工会会员进行排序
     *
     * @param pageIndex 页索引
     * @param pageSize  页大小
     * @return 获奖个人表列表
     * @author wangping
     * @date 2021-04-09 15:02:18
     */
    JsonResult<UserListVO> getUserList(int pageIndex, int pageSize, String wxUserName);

    /**
     * 用户加入团队
     *
     * @param teamId
     * @return com.yitu.mayday.api.vo.JsonResult
     * @author wangping
     * @date 2021/4/9 15:56
     */
    JsonResult userAddTeam(int teamId);


    /**
     * 获取团队信息详情以及团队下的用户所拥有的积分排行（分页）
     *
     * @param pageIndex 页索引
     * @param pageSize  页大小
     * @param teamId    团队id
     * @return com.yitu.mayday.api.vo.JsonResult<com.yitu.mayday.api.vo.userMedal.TeamUserListVO>
     * @author wangping
     * @date 2021/4/10 10:04
     */
    JsonResult<TeamUserListVO> getTeamById(int teamId, int pageIndex, int pageSize);

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
    JsonResult<TeamScoreVO> findScoreSumByType(Integer type, int pageIndex, int pageSize, int teamType);

    /**
     * 保存团队总榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/8 17:20
     */
    void saveTeamScoreRankingToRedis();

    /**
     * 保存团队周榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/9 18:39
     */
    void saveTeamScoreWeekRankingToRedis();

    /**
     * 根据团队id查找团队
     *
     * @param teamId 团队id
     * @return com.yitu.cpcFounding.api.domain.Team
     * @author qixinyi
     * @date 2021/6/17 9:50
     */
    Team findTeamById(int teamId);
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
    JsonResult<TeamScoreVO> findNextLevelScoreSum(int pageIndex, int pageSize, Long parentId,Integer rankingType);

    /**
     * 保存街道总榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/30 9:02
     */
    void saveStreetScoreRankingToRedis();

    /**
     * 保存街道周榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/30 9:02
     */
    void saveStreetScoreWeekRankingToRedis();

    /**
     * 保存片区总榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/30 9:02
     */
    void saveAreaScoreRankingToRedis();

    /**
     * 保存片区周榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/30 9:02
     */
    void saveAreaScoreWeekRankingToRedis();
}
