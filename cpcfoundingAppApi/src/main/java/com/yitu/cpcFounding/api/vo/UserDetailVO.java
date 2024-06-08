package com.yitu.cpcFounding.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户我的页面详情实体
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/7 16:54
 */

@Data
@ApiModel("用户我的页面详情实体对象")
public class UserDetailVO {
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("微信头像")
    private String wxHeadPic;

    @ApiModelProperty("总积分")
    private Integer scoreTotal;

    @ApiModelProperty("个人周排名")
    private Integer userWeekRanking;

    @ApiModelProperty("个人总排名")
    private Integer userRanking;

    @ApiModelProperty("团队周排名")
    private Integer teamWeekRanking;

    @ApiModelProperty("团队总排名")
    private Integer teamRanking;

    @ApiModelProperty("答题榜排名")
    private Integer answerRanking;

    @ApiModelProperty("周时间范围")
    private String time;
}
