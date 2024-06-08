package com.yitu.cpcFounding.api.service;

import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.TeamScoreVO;
import com.yitu.cpcFounding.api.vo.UserScoreDetailVO;
import com.yitu.cpcFounding.api.vo.UserScoreVO;
import com.yitu.cpcFounding.api.vo.userScore.UserScoreLabelVO;
import com.yitu.cpcFounding.api.vo.userScore.UserScoreTimesOfDay;
import com.yitu.cpcFounding.api.vo.userScore.UserScoreTotalVO;
import com.yitu.cpcFounding.api.vo.userScore.UserScoresOfDay;

import java.util.List;

/**
 * 用户积分表服务
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/7 14:54
 */

public interface UserScoreService {
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
    JsonResult<UserScoreVO> findScoreSumByType(Integer type, int pageIndex, int pageSize);

    /**
     * 获取积分明细
     *
     * @param pageSize  显示条数
     * @param pageIndex 页码
     * @return com.yitu.cpcFounding.api.vo.JsonResult<java.util.List < com.yitu.cpcFounding.api.vo.UserScoreDetailVO>>
     * @author qixinyi
     * @date 2021/6/7 15:55
     */
    JsonResult<List<UserScoreDetailVO>> findUserScoreDetails(int pageIndex, int pageSize);

    /**
     * 获取答题榜
     *
     * @param pageSize  显示条数
     * @param pageIndex 页码
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.UserScoreVO>
     * @author qixinyi
     * @date 2021/6/9 10:34
     */
    JsonResult<UserScoreVO> findAnswerScoreSum(int pageIndex, int pageSize);

    /**
     * 保存用户总榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/8 10:09
     */
    void saveUserScoreRankingToRedis();

    /**
     * 保存用户周榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/8 10:09
     */
    void saveUserScoreWeekRankingToRedis();

    /**
     * 保存答题总榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/9 10:07
     */
    void saveAnswerScoreRankingToRedis();

    /**
     * 获取当日积分
     *
     * @param
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.userScore.UserScoreTotalOfDayVO>
     * @author qixinyi
     * @date 2021/6/10 16:10
     */
    JsonResult<UserScoreTotalVO> findScoreTotalOfDay();

    /**
     * 加积分
     *
     * @param type     积分类型
     * @param targetId 关联id(source_type为时1 党历史id 3 答题编号 4分享记录id 6 奖品id 7时为团队id)
     * @param score    积分数
     * @return com.yitu.cpcFounding.api.vo.JsonResult<java.lang.String>
     * @author qixinyi
     * @date 2021/6/16 18:46
     */
    public JsonResult<String> addScore(Integer type, Long targetId, Integer score);

    /**
     * 获取每日积分明细
     *
     * @param
     * @return com.yitu.cpcFounding.api.vo.JsonResult<UserScoresOfDay>
     * @author qixinyi
     * @date 2021/6/11 16:14
     */
    JsonResult<UserScoresOfDay> findUserScoresOfDay();

    /**
     * 每日首次分享和签到加积分
     *
     * @param sourceType 积分来源类型
     * @return com.yitu.cpcFounding.api.vo.JsonResult
     * @author qixinyi
     * @date 2021/6/18 17:16
     */
    JsonResult addShareAndSignInScore(Integer sourceType);

    /**
     * 获取排行榜提示语
     *
     * @param rankingType
     * @return com.yitu.cpcFounding.api.vo.userScore.UserScoreLabelVO
     * @author wangping
     * @date 2021/6/28 15:47
     */
    UserScoreLabelVO getRankingLabel(int rankingType);

    /**
     * 获取每日次数明细
     *
     * @param
     * @return com.yitu.cpcFounding.api.vo.JsonResult<UserScoresOfDay>
     * @author qixinyi
     * @date 2021/6/11 16:14
     */
    JsonResult<UserScoreTimesOfDay> findUserScoreTimesOfDay();
}
