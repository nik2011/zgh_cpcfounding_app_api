package com.yitu.cpcFounding.api.vo.modelWorker;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 劳模详情vo
 * @author wangping
 * @version 1.0
 * @date 2021/04/09
 */
@Data
@ApiModel("劳模详情VO")
public class ModelWorkerDetailVO {

    @ApiModelProperty(value = "劳模id")
    @JsonSerialize(using = ToStringSerializer.class)
    private long id;

    @ApiModelProperty(value = "名字")
    private String name;

    /**文本标签*/
    @ApiModelProperty(value = "文本标签")
    private String label;

    /**介绍类型：1图文详情，2 视频*/
    @ApiModelProperty(value = "介绍类型：1图文详情，2 视频")
    private Integer introduceType;


    /**头像地址*/
    @ApiModelProperty(value = "头像地址")
    private String headPic;

    /**介绍*/
    @ApiModelProperty(value = "介绍")
    private String introduce;

    /**视频地址*/
    @ApiModelProperty(value = "视频地址")
    private String videoUrl;


}
