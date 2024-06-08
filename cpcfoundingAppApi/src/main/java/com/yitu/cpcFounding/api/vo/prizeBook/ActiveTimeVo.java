package com.yitu.cpcFounding.api.vo.prizeBook;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Value;

import java.util.Date;

/**
 * @author liuzhaowei
 * @date 2021/1/23
 */
@Data
@ApiModel("活动开始结束时间")
public class ActiveTimeVo {

    /**
     * 状态 0 未开始  1 进行中  2 已结束
     */
    @ApiModelProperty(" 状态 0 未开始  1 进行中  2 已结束")
    private Integer status;

    /**
     * 开始时间
     */
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date beginDate;

    /**
     * 结束时间
     */
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endDate;


}
