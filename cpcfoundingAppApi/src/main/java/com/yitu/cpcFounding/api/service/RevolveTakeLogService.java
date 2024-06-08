package com.yitu.cpcFounding.api.service;

import com.yitu.cpcFounding.api.domain.RevolveTakeLog;
import com.yitu.cpcFounding.api.enums.prize.ShakeTakeLogTypeEnum;

/**
 * 用户摇奖消费日志表服务
 *
 * @author shenjun
 * @date 2021-01-21 12:01:28
 */
public interface RevolveTakeLogService {

    /**
     * @param type,类型
     * @param inOrOut,加或减
     * @param userPrizeId 用户奖品id
     * @param userId,
     * @param userName
     * @return int
     * @Description: 添加记录
     * @author liuzhaowei
     * @date 2021/1/22
     */
    int insertRecord(ShakeTakeLogTypeEnum type, int inOrOut, long userPrizeId, long userId, String userName);

    /**
     * @param type,类型
     * @param inOrOut,加或减
     * @param userPrizeId 用户奖品id
     * @param userId,
     * @param userName
     * @return int
     * @Description: 封装实体
     * @author liuzhaowei
     * @date 2021/1/22
     */
    RevolveTakeLog packageEntity(ShakeTakeLogTypeEnum type, int inOrOut, long userPrizeId, long userId, String userName);
}
