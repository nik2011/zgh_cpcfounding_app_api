package com.yitu.cpcFounding.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户表
 * @author yaoyanhua
 * @date 2021-01-21 11:02:19
 */
@Data
public class UserVO {
    /**
     * sessionId
     */
    @ApiModelProperty("登录token")
    private String sessionId;

    /**
    * 主键
    */
    @ApiModelProperty("用户id")
    private Long id;

    /**
    * 微信号id
    */
    @ApiModelProperty("微信号id")
    private String wxOpenid;

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
    * 微信手机号
    */
    @ApiModelProperty("微信手机号")
    private String wxPhone;

    /**
    * 工会手机号
    */
    @ApiModelProperty("工会手机号")
    private String phone;

    /**
    * 会员状态 1 工会会员 0不是会员
    */
    @ApiModelProperty("会员状态：1工会会员 0不是会员")
    private Integer memberState;

    /**
    * 审核不通过次数
    */
    @ApiModelProperty("审核不通过次数")
    private Integer auditFailNum;


    /**
     * 用户中奖礼品id
     */
    @ApiModelProperty("用户中奖礼品id")
    private String userWinningPrizeId;


    /**
     * 团队id
     */
    @ApiModelProperty("团队id")
    private int teamId;

}