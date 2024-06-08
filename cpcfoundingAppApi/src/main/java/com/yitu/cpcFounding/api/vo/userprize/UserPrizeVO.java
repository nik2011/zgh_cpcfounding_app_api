package com.yitu.cpcFounding.api.vo.userprize;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户奖品列表vo
 * @author chenpinjia
 * @version 1.0
 * @date 2021/1/21
 */
@Data
@ApiModel("用户奖品列表VO")
public class UserPrizeVO {

    @ApiModelProperty(value = "用户奖品记录id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "订单号")
    private String orderNo;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "头像图片地址")
    private String wxHeadPic;

    @ApiModelProperty(value = "奖品id")
    private Long prizeId;

    @ApiModelProperty(value = "中奖时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date addDate;

    @ApiModelProperty(value = "活动类型枚举")
    private Integer activeType;

    @ApiModelProperty(value = "活动类型")
    private String activeTypeStr;

    @ApiModelProperty(value = "奖品类型 1.实物  2 虚拟 3 现金 4.酒店 5 次数增加  6什么都没有 7代金券")
    private Integer prizeType;

    @ApiModelProperty(value = "奖品标题")
    private String title;

    @ApiModelProperty(value = "图片连接")
    private String imageUrl;

    @ApiModelProperty(value = "领取状态：0待领取、1已领取、2已失效")
    private Integer getStatus;

}
