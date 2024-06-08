package com.yitu.cpcFounding.api.service;

import com.yitu.cpcFounding.api.vo.UserVO;
import com.yitu.cpcFounding.api.vo.redis.DynamicUserVO;
import com.yitu.cpcFounding.api.vo.redis.YaoPrizeBookVo;


import java.util.List;

/**
 * 公共服务
 *
 * @author yaoyanhua
 * @date 2020/7/24
 */
public interface CommonService {
    /**
     * 获取缓存用户
     *
     * @param sessionId
     * @return com.yitu.hotel.scheduler.domain.RUser
     * @author yaoyanhua
     * @date 2020/12/28 2:02
     */
    UserVO getLoginUser(String sessionId);

    /**
     * 更新或插入缓存用户
     *
     * @param sessionId
     * @param loginUser
     * @return boolean
     * @author yaoyanhua
     * @date 2020/12/28 2:11
     */
    boolean setLoginUser(String sessionId, UserVO loginUser);

    /**
     * @param userId
     * @return int
     * @Description: 获取用户摇一摇次数
     * @author liuzhaowei
     * @date 2021/1/22
     */
    int getUserShakeCount(long userId, String sessionId);

    /**
     * @param userId
     * @return int
     * @Description: 获取用户摇一摇次数 没有初始化
     * @author liuzhaowei
     * @date 2021/1/22
     */
    Object getUserShakeCount(long userId);

    /**
     * @param userId
     * @return int
     * @Description: 增加一次摇一摇机会
     * @author liuzhaowei
     * @date 2021/1/22
     */
    void incrOneUserYaoCount(long userId, String sessionId);

    /**
     * @param userId
     * @return int
     * @Description: 减少一次摇一摇机会
     * @author liuzhaowei
     * @date 2021/1/22
     */
    long decrOneUserYaoCount(long userId);

    /**
     * @param
     * @return int
     * @Description: 获取摇一摇奖品列表
     * @author liuzhaowei
     * @date 2021/1/22
     */
    List<YaoPrizeBookVo> getYaoPrizeList();

    /**
     * @param
     * @return int
     * @Description: 设置摇一摇奖品列表
     * @author liuzhaowei
     * @date 2021/1/22
     */
    boolean setYaoPrizeList();

    /**
     * @param
     * @return int
     * @Description: 获取奖品未中奖概率
     * @author liuzhaowei
     * @date 2021/1/22
     */
    List<Integer> getNoWinningProbability();

    /**
     * @param
     * @return int
     * @Description: 设置奖品未中奖概率
     * @author liuzhaowei
     * @date 2021/1/22
     */
    boolean setNoWinningProbability();

    /**
     * @param prizeId
     * @return int
     * @Description: 获取摇一摇奖品库存
     * @author liuzhaowei
     * @date 2021/1/22
     */
    int getPrizeStockCount(long prizeId);

    /**
     * @param
     * @return int
     * @Description: 设置摇一摇奖品库存
     * @author liuzhaowei
     * @date 2021/1/22
     */
    boolean setPrizeStockCount();

    /**
     * @param prizeId
     * @return int
     * @Description: 摇一摇奖品库存加一
     * @author liuzhaowei
     * @date 2021/1/22
     */
    long incrPrizeStockCount(long prizeId);

    /**
     * @param prizeId
     * @return int
     * @Description: 摇一摇奖品库存减一
     * @author liuzhaowei
     * @date 2021/1/22
     */
    long decrPrizeStockCount(long prizeId);

    /**
     * @param userId
     * @return int
     * @Description: 获取用户动态信息
     * @author liuzhaowei
     * @date 2021/1/22
     */
    DynamicUserVO getUserDynamicInfo(long userId, String sessionId);

    /**
     * @param userId
     * @return int
     * @Description: 设置用户动态信息
     * @author liuzhaowei
     * @date 2021/1/22
     */
    boolean setUserDynamicInfo(long userId, DynamicUserVO userVO);

    /**
     * @param userId
     * @return int
     * @Description: 获取用户当天可以中奖次数
     * @author liuzhaowei
     * @date 2021/4/29
     */
    int getUserPrizeCount(long userId);

    /**
     * @param userId
     * @return int
     * @Description: 减一次用户中奖次数
     * @author liuzhaowei
     * @date 2021/1/22
     */
    boolean decrOneUserPrizeCount(long userId);

    /**
     * @param userId
     * @return int
     * @Description: 增加一次用户中奖次数
     * @author liuzhaowei
     * @date 2021/1/22
     */
    boolean incrOneUserPrizeCount(long userId);

    /**
     * @param
     * @return java.util.List<java.lang.String>
     * @Description:获取用户答题头像
     * @author liuzhaowei
     * @date 2021/5/17
     */
    List<String> getUserAnswerHeaderList();


    /**
    * 获取用户答题数量
    * @author shenjun
    * @date   2021/7/19 14:57
    * @return
    */
    long getUserAnswerCount();

}
