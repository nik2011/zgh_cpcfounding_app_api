package com.yitu.cpcFounding.api.vo.userScore;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

/**
 * 个人排行榜
 *
 * @author qixinyi
 * @date 2021-04-10 11:02:19
 */
@ApiModel("个人排行榜")
@Data
public class UserRankingListVO {
    /**
     * 主键
     */
    @ApiModelProperty("用户id")
    private Long id;

    /**
     * 微信昵称
     */
    @ApiModelProperty("微信昵称")
    private String wxUserName;

    /**
     * 头像图片地址
     */
    @ApiModelProperty("头像图片地址")
    private String wxHeadPic;

    /**
     * 积分总数
     */
    @ApiModelProperty("积分总数")
    private int scoreTotal;

    /**
     * 当前用户所在排行
     */
    @ApiModelProperty("当前用户所在排行")
    private Integer userSort;
}