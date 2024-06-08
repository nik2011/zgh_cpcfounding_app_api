package com.yitu.cpcFounding.api.vo.redis;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * redis用户表
 *
 * @author yaoyanhua
 * @date 2021-01-21 11:02:19
 */
@Setter
@Getter
public class DynamicUserVO {

    /**
     * 摇一摇次数
     */
    @ApiModelProperty("摇一摇次数")
    private int yaoCount;
    /**
     * 用户今天中奖礼品id
     */
    @ApiModelProperty("用户今天中奖礼品id ")
    private String userTodayWinningPrizeId;

    @ApiModelProperty("当天获得摇一摇次数")
    private int getYaoCount;

    @ApiModelProperty("当天用户是否答题")
    private Boolean whetherAnswer;

    /**
     * 答题次数
     */
    @ApiModelProperty("答题次数")
    private int answerCount;

}