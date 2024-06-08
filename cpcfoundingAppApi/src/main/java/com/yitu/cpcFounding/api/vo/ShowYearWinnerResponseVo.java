package com.yitu.cpcFounding.api.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 创建人
 * LsYearApiSystem
 * 2021/1/27 14:33
 *
 * @author luzhichao
 **/
@Data
@ApiModel(value="晒年味获奖信息",description="晒年味获奖信息")
public class ShowYearWinnerResponseVo {

    @ApiModelProperty("获奖列表")
    IPage<PrizeWinnerVO> topList;

    @JsonInclude(value= JsonInclude.Include.NON_NULL)
    @ApiModelProperty("当前登入用户点赞信息")
    PrizeWinnerVO self;

}
