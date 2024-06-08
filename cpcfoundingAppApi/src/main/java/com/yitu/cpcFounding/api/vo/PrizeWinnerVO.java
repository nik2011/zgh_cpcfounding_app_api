package com.yitu.cpcFounding.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 晒年味获奖用户信息
 * LsYearApiSystem
 * 2021/1/21 10:14
 *
 * @author luzhichao
 **/
@Data
@ApiModel(value="晒年味获奖用户信息",description="晒年味获奖用户信息")
public class PrizeWinnerVO implements Serializable {

    private static final long serialVersionUID = 6185040964228279496L;

    @ApiModelProperty("用户id")
    private Integer userId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("点赞数")
    private Long likeNum;

    @ApiModelProperty("排名")
    private Long orderIndex;

    @ApiModelProperty("头像")
    private String headPic;

    @ApiModelProperty("得奖标识 Y/N")
    private String winFlag;

}
