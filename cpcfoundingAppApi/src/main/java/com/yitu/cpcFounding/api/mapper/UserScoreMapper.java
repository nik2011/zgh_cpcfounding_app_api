package com.yitu.cpcFounding.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yitu.cpcFounding.api.domain.UserScore;
import com.yitu.cpcFounding.api.vo.UserScoreDetailVO;
import com.yitu.cpcFounding.api.vo.UserScoreDetailsVO;
import com.yitu.cpcFounding.api.vo.userScore.UserRankingListVO;
import com.yitu.cpcFounding.api.vo.userScore.UserScoreTotalVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 用户积分表Mapper
 *
 * @author shenjun
 * @date 2021-06-03 10:25:15
 */
@Mapper
public interface UserScoreMapper extends BaseMapper<UserScore> {
    /**
     * 根据用户id获取用户积分列表
     *
     * @param page
     * @param userId 用户id
     * @param date   积分日期
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.yitu.cpcFounding.api.vo.UserScoreDetailsVO>
     * @author qixinyi
     * @date 2021/6/7 16:15
     */
    IPage<UserScoreDetailsVO> selectUserScoreDetails(Page<UserScoreDetailVO> page, @Param("userId") long userId, @Param("date") String date);

    /**
     * 根据用户id获取添加时间列表
     *
     * @param userId 用户id
     * @return java.util.List<java.util.Date>
     * @author qixinyi
     * @date 2021/6/7 16:16
     */
    List<Date> findAddDateByUserId(@Param("userId") long userId);

    /**
     * 获取用户总榜
     *
     * @param index 页码
     * @param size  页大小
     * @return java.util.List<com.yitu.cpcFounding.api.vo.userScore.UserScoreVO>
     * @author qixinyi
     * @date 2021/6/8 14:28
     */
    List<UserRankingListVO> selectUserScoreRanking(@Param("index") int index, @Param("size") int size);

    /**
     * 获取答题总榜
     *
     * @param index
     * @param size
     * @return java.util.List<com.yitu.cpcFounding.api.vo.userScore.UserRankingListVO>
     * @author qixinyi
     * @date 2021/6/9 10:14
     */
    List<UserRankingListVO> selectAnswerScoreRanking(@Param("index") int index, @Param("size") int size);

    /**
     * 获取用户周榜
     *
     * @param index     页码
     * @param size      页大小
     * @param beginDate 开始时间
     * @param endDate   结束时间
     * @return java.util.List<com.yitu.cpcFounding.api.vo.userScore.UserRankingListVO>
     * @author qixinyi
     * @date 2021/6/9 16:35
     */
    List<UserRankingListVO> selectUserScoreWeekRanking(@Param("index") int index, @Param("size") int size, @Param("beginDate") String beginDate, @Param("endDate") String endDate);

    /**
     * 获取用户积分
     *
     * @param id 用户id
     * @return com.yitu.cpcFounding.api.vo.userScore.UserScoreTotalOfDayVO
     * @author qixinyi
     * @date 2021/6/10 16:12
     */
    UserScoreTotalVO selectScoreTotalOfDay(@Param("id") Long id);

    /**
     * 根据积分类型和用户id计算当天积分总数
     *
     * @param sourceType 积分类型
     * @param id         用户id
     * @return java.lang.Integer
     * @author qixinyi
     * @date 2021/6/11 17:48
     */
    Integer selectSumOfDayByScoreTypeAndUserId(@Param("sourceType") int sourceType, @Param("id") Long id);

    /**
     * 根据用户添加时间和用户id查询用户当天积分数
     *
     * @param userId   用户id
     * @param realDate 添加时间
     * @return java.lang.Integer
     * @author qixinyi
     * @date 2021/6/16 10:23
     */
    Integer selectSumOfDayByAddDate(@Param("userId") long userId, @Param("realDate") String realDate);

    /**
     * 根据用户添加时间和用户id查询用户当天积分数
     *
     * @param beInviteUserId 被邀请答题用户id
     * @param inviteUserId   邀请用户id
     * @return java.lang.Integer
     * @author qixinyi
     * @date 2021/6/16 10:23
     */
    Integer checkUserShareAnswer(@Param("beInviteUserId") long beInviteUserId, @Param("inviteUserId") long inviteUserId);

    /**
     * 获取每日次数
     *
     * @param sourceType 积分来源类型
     * @param id         用户id
     * @return java.lang.Integer
     * @author qixinyi
     * @date 2021/7/2 16:00
     */
    Integer selectTimesOfDayByTypeAndUserId(@Param("sourceType") int sourceType, @Param("id") Long id);

    /**
     * 获取用户总榜列表
     *
     * @param page
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.yitu.cpcFounding.api.vo.userScore.UserRankingListVO>
     * @author qixinyi
     * @date 2021/7/16 22:05
     */
    IPage<UserRankingListVO> selectUserScoreRankings(Page<UserRankingListVO> page);

    /**
     * 获取用户周榜列表
     *
     * @param page
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.yitu.cpcFounding.api.vo.userScore.UserRankingListVO>
     * @author qixinyi
     * @date 2021/7/16 22:14
     */
    IPage<UserRankingListVO> selectUserScoreWeekRankings(Page<UserRankingListVO> page);

    /**
     * 获取用户答题榜列表
     *
     * @param page
     * @return com.baomidou.mybatisplus.core.metadata.IPage<com.yitu.cpcFounding.api.vo.userScore.UserRankingListVO>
     * @author qixinyi
     * @date 2021/7/16 22:14
     */
    IPage<UserRankingListVO> selectAnswerScoreRankings(Page<UserRankingListVO> page);
}
