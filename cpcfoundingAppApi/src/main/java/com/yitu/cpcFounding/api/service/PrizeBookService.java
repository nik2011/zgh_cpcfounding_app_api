package com.yitu.cpcFounding.api.service;

import com.yitu.cpcFounding.api.vo.prizeBook.PrizeBookVO;
import com.yitu.cpcFounding.api.vo.prizeBook.ActiveTimeVo;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.redis.YaoPrizeBookVo;

import java.util.List;

/**
 * 奖品库表服务
 * @author shenjun
 * @date 2021-01-21 11:47:32
 */
public interface PrizeBookService {

    /**
     * @return JsonResult
     * @Description: 获取摇一摇数量
     * @author liuzhaowei
     * @date 2021/1/21
     */
    JsonResult getYaoYiYaoCountRedis();

//     /**
//     * @Description: 获取摇一摇数量
//     * @param
//     * @author liuzhaowei
//     * @date 2021/1/21
//     * @return JsonResult
//      */
//     @Deprecated
//     JsonResult getYaoYiYaoCount();

//    /**
//     * @Description: 执行摇一摇
//     * @param
//     * @author liuzhaowei
//     * @date 2021/1/21
//     * @return JsonResult
//     */
//    JsonResult<YaoPrizeBookVo> doShake();

    /**
     * @Description: 执行摇一摇
     * @param
     * @author liuzhaowei
     * @date 2021/1/21
     * @return JsonResult
     */
    JsonResult<YaoPrizeBookVo> doShakeRedis();

//    /**
//     * @Description: 处理摇一摇中奖记录队列
//     * @param data
//     * @author liuzhaowei
//     * @date 2021/1/28
//     * @return void
//     */
//    void handelShakeWinningQueue(String data);

    /**
     * 根据活动类型查询奖品列表
     *
     * @param activeType 活动类型
     * @return JsonResult<java.util.List<PrizeBookVO>>
     * @author qinmingtong
     * @date 2021/1/23 8:52
     */
    JsonResult<List<PrizeBookVO>> prizeBookList(Integer activeType);

    /**
    * @Description: 获取活动开始结束时间
    * @param
    * @author liuzhaowei
    * @date 2021/1/23
    * @return JsonResult<ActiveTimeVo>
     */
    JsonResult<ActiveTimeVo> getActiveTime(int activeType);

}
