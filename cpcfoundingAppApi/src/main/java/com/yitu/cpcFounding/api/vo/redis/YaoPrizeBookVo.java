package com.yitu.cpcFounding.api.vo.redis;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 奖品库表
 *
 * @author shenjun
 * @date 2021-01-21 11:02:18
 */
@Data
@ApiModel("奖品库Vo")
public class YaoPrizeBookVo {
    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private Integer id;

    /**
     * 活动类型 1.摇一摇活动 2.晒年味 3.暖城活动
     */
    @ApiModelProperty("活动类型 1.摇一摇活动 2.晒年味 3.暖城活动")
    private Integer activeType;

    /**
     * 标题
     */
    @ApiModelProperty("标题")
    private String title;

    /**
     * 奖品类型 1.实物  2 虚拟 3 现金 4.酒店 5 次数增加  6什么都没有 7代金券
     */
    @ApiModelProperty("奖品类型 1.实物  2 虚拟 3 现金 4.酒店 5 次数增加  6什么都没有 7代金券")
    private Integer prizeType;

    /**
     * 库存
     */
    @ApiModelProperty("库存")
    private Integer stock;

    /**
     * 图片
     */
    @ApiModelProperty("图片")
    private String imageUrl;

    /**
     * 是否工会奖品  1普通、2工会
     */
    @ApiModelProperty("是否工会奖品  1普通、2工会")
    private Integer memberType;

    /**
     * 奖品值（现金用到
     */
    @ApiModelProperty("奖品值（现金用到")
    private Integer prizeValue;

    /**
     * 中奖概率开始值
     */
    @ApiModelProperty("中奖概率开始值")
    private Integer chanceBeginNum;

    /**
     * 中奖概率结束值
     */
    @ApiModelProperty("中奖概率结束值")
    private Integer chanceEndNum;


}