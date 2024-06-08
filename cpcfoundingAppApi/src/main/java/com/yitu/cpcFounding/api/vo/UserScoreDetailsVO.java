package com.yitu.cpcFounding.api.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户积分明细列表实体
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/7 15:49
 */

@Data
@ApiModel("用户积分明细列表实体对象")
public class UserScoreDetailsVO {
    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("积分id")
    private Long id;

    /**
     * 积分来源类型:1签到；2阅读文章；3每日答题；4每日首次分享；5邀请好友完成答题；6奖励得分 7 加入团队
     */
    @TableField("source_type")
    @ApiModelProperty("积分来源类型:1签到；2阅读文章；3每日答题；4每日首次分享；5邀请好友完成答题；6奖励得分 7 加入团队")
    private Integer sourceType;

    @ApiModelProperty("积分来源类型名")
    private String sourceTypeName;

    /**
     * 积分数
     */
    @TableField("score")
    @ApiModelProperty("积分数")
    private Integer score;

    /**
     * 创建时间
     */
    @TableField("add_date")
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    private Date addDate;
}
