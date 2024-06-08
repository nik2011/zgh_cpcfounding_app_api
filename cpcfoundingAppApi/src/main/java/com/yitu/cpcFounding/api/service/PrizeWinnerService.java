package com.yitu.cpcFounding.api.service;

import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.ShowYearWinnerResponseVo;

/**
 * 获奖用户表服务
 * @author luzhichao
 * @date 2021-01-21 11:02:18
 */
public interface PrizeWinnerService {

	/**
     * 获取晒获奖用户表列表数据（已经生成获奖名单）
     * @param pageIndex 页索引
     * @param pageSize 页大小
     * @author luzhichao
     * @date 2021-01-21 11:02:18
     * @return 获奖用户表列表前端实体
     */
    JsonResult<ShowYearWinnerResponseVo> getShowYearWinnerList(int pageIndex, int pageSize);



}
