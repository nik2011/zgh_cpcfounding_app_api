package com.yitu.cpcFounding.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yitu.cpcFounding.api.domain.Team;
import com.yitu.cpcFounding.api.vo.team.TeamRankingListVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 团队Mapper
 *
 * @author wangping
 * @date 2021/4/9 11:53
 */
@Mapper
public interface TeamMapper extends BaseMapper<Team> {
    /**
     * 获取团队总榜
     *
     * @param index 页码
     * @param size  页大小
     * @param type  类型
     * @return java.util.List<com.yitu.cpcFounding.api.vo.team.TeamRankingListVO>
     * @author qixinyi
     * @date 2021/6/16 16:44
     */
    List<TeamRankingListVO> selectTeamScoreRanking(@Param("index") int index, @Param("size") int size, @Param("type") Integer type);

    /**
     * 加入团队修改团队的积分数、团队人数、当前周积分
     *
     * @param id
     * @param scoreTotal 个人总积分
     * @param score      加入团队个人加分
     * @return int
     * @author wangping
     * @date 2021/6/15 11:46
     */
    int updateTeamScorePeople(@Param("id") Long id, @Param("scoreTotal") int scoreTotal, @Param("score") int score);

    /**
     * 根据团队id获取团队周积分
     *
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @param id        团队id
     * @return java.lang.Integer
     * @author qixinyi
     * @date 2021/6/16 17:28
     */
    Integer selectTeamScoreWeekRankingById(@Param("beginDate") String beginDate, @Param("endDate") String endDate, @Param("id") Long id);

    /**
     * 获取团队列表
     *
     * @param index 页码
     * @param size  页大小
     * @param type  类型
     * @return java.util.List<com.yitu.cpcFounding.api.domain.Team>
     * @author qixinyi
     * @date 2021/6/16 18:16
     */
    List<Team> selectTeams(@Param("index") int index, @Param("size") int size, @Param("type") int type);

    /**
     * 根据团队id获取团队月积分
     *
     * @param id 团队id
     * @return java.lang.Integer
     * @author qixinyi
     * @date 2021/6/17 16:23
     */
    Integer selectTeamScoreRankingById(@Param("id") long id);

    /**
     * 根据类型查询团队列表
     *
     * @param type 类型
     * @return java.util.List<com.yitu.cpcFounding.api.vo.team.TeamRankingListVO>
     * @author qixinyi
     * @date 2021/6/28 17:17
     */
    List<TeamRankingListVO> selectByType(@Param("type") int type);

    /**
     * 根据父级id查询团队列表
     *
     * @param id 父级id
     * @return java.util.List<com.yitu.cpcFounding.api.domain.Team>
     * @author qixinyi
     * @date 2021/6/28 17:24
     */
    List<Team> selectByParentId(@Param("id") Long id);

    /**
     * 根据条件计算团队周积分
     *
     * @param id        团队id
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return java.lang.Integer
     * @author qixinyi
     * @date 2021/6/28 17:31
     */
    Integer selectScoreTotalByTimeAndId(@Param("id") long id, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 获取下级总榜
     *
     * @param parentId 父级id
     * @return java.util.List<com.yitu.cpcFounding.api.vo.team.TeamRankingListVO>
     * @author qixinyi
     * @date 2021/7/2 12:06
     */
    List<TeamRankingListVO> getNextLevelRankingByParentIdAndType(@Param("parentId") Long parentId);

    /**
     * 获取下级周榜
     *
     * @param parentId 父级id
     * @return java.util.List<com.yitu.cpcFounding.api.vo.team.TeamRankingListVO>
     * @author qixinyi
     * @date 2021/7/2 14:02
     */
    List<TeamRankingListVO> getNextLevelWeekRankingByParentIdAndType(@Param("parentId") Long parentId);

    /**
     * 获取街道片区周榜
     *
     * @param type  类型
     * @param index 页码
     * @param size  页大小
     * @return java.util.List<com.yitu.cpcFounding.api.vo.team.TeamRankingListVO>
     * @author qixinyi
     * @date 2021/6/22 11:28
     */
    List<TeamRankingListVO> selectStreetAndAreaScoreWeekRanking(@Param("index") int index, @Param("size") int size, @Param("type") int type);

    /**
     * 获取团队/街道/片区总榜列表
     *
     * @param page
     * @param type 类型
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.yitu.cpcFounding.api.vo.team.TeamRankingListVO>
     * @author qixinyi
     * @date 2021/7/17 0:14
     */
    IPage<TeamRankingListVO> selectTeamScoreRankings(Page<TeamRankingListVO> page, @Param("type") Integer type);

    /**
     * 获取团队/街道/片区周榜列表
     *
     * @param page
     * @param type 类型
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.yitu.cpcFounding.api.vo.team.TeamRankingListVO>
     * @author qixinyi
     * @date 2021/7/17 0:14
     */
    IPage<TeamRankingListVO> selectTeamScoreWeekRankings(Page<TeamRankingListVO> page, @Param("type") Integer type);
}
