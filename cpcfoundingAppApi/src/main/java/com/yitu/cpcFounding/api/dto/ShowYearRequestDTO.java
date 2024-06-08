package com.yitu.cpcFounding.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 晒年味请求参
 * LsYearApiSystem
 * 2021/1/21 9:54
 *
 * @author luzhichao
 **/
@Data
@ApiModel(value="晒年味请求参",description="晒年味请求参")
public class ShowYearRequestDTO {

    @ApiModelProperty("编号")
    private long id;

    @ApiModelProperty("当前页")
    private int pageIndex;

    @ApiModelProperty("每页条数")
    private int pageSize;

    @ApiModelProperty("排序类型：0：点赞数 1：时间倒序")
    private int sortType;
}
