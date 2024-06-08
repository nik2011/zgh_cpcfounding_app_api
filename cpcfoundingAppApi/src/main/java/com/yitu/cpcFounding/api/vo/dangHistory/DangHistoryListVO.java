package com.yitu.cpcFounding.api.vo.dangHistory;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 *
 * 党史列表信息
 * @author wangping
 * @date 2021/6/7 16:04
 */
@Data
@ApiModel("党史列表信息")
public class DangHistoryListVO {

    @ApiModelProperty(value = "党史id")
    @JsonSerialize(using = ToStringSerializer.class)
    private long id;

    @ApiModelProperty(value = "党史标题")
    private String title;

    @ApiModelProperty(value = "类型：1 文章 2 视频")
    private Integer type;

    @ApiModelProperty(value = "封面类型：1无图 2单图 3三图 4 宽图")
    private Integer coverType;

    @ApiModelProperty(value = "图片地址集合多图逗号隔开/封面")
    private List<String> url;

    /**
     * 是否置顶 1置顶 0未置顶
     */
    @ApiModelProperty("是否置顶 1置顶 0未置顶")
    private Integer top;


    @ApiModelProperty(value = "发布时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", timezone = "GMT+8")
    private Date addDate;

}
