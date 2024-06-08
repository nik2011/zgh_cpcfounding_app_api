package com.yitu.cpcFounding.api.vo.prizeBook;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 奖品库表
 *
 * @author qinmingtong
 * @date 2021-01-21 16:08:18
 */
@Data
@ApiModel("奖品库表VO")
public class PrizeBookVO {

    @ApiModelProperty(value = "活动类型 1.摇一摇活动 2.晒年味 3.暖城活动")
    private Integer activeType;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "奖品类型 1.实物  2 虚拟 3 现金 4.酒店 5 次数增加  6什么都没有 7代金券 8 虚拟奖章")
    private Integer prizeType;

    @ApiModelProperty(value = "图片")
    private String imageUrl;

    @ApiModelProperty(value = "工会奖品类型  1普通、2工会")
    private Integer memberType;

    @ApiModelProperty(value = "有效期")
    private Date expirationDate;

    @ApiModelProperty(value = "说明")
    private String remark;

    @ApiModelProperty(value = "是否删除：0未删除、1已删除")
    private Integer deleted;

    @ApiModelProperty(value = "说明")
    private String explainContent;

}
