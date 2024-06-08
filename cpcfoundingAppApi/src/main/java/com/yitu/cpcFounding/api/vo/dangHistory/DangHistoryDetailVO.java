package com.yitu.cpcFounding.api.vo.dangHistory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 *
 * 党史详情信息
 * @author wangping
 * @date 2021/6/7 16:04
 */
@Data
@ApiModel("党史详情信息")
public class DangHistoryDetailVO {

    @ApiModelProperty(value = "党史id")
    @JsonSerialize(using = ToStringSerializer.class)
    private long id;

    @ApiModelProperty(value = "党史标题")
    private String title;

    @ApiModelProperty(value = "内容/视频地址")
    private String content;

    @ApiModelProperty(value = "类型：1 文章 2 视频 3公众号")
    private Integer type;

    /**
     * 是否置顶 1置顶 0未置顶
     */
    @ApiModelProperty(value = "是否置顶 1置顶 0未置顶")
    private Integer top;

    @ApiModelProperty(value = "阅读量")
    private Integer visitNum;

    @ApiModelProperty(value = "发布人")
    private String addUser;


    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date addDate;

}
