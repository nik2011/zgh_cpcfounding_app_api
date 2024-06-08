package com.yitu.cpcFounding.api.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户积分明细实体
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/7 15:45
 */

@Data
@ApiModel("用户积分明细实体对象")
public class UserScoreDetailVO {
    /**
     * 当日积分
     */
    @ApiModelProperty("当日积分")
    private Integer sum;

    /**
     * 创建时间
     */
    @TableField("add_date")
    @ApiModelProperty("时间")
    @JsonFormat(pattern = "Y.M.d", timezone = "GMT+8")
    private Date addDate;

    @ApiModelProperty("用户积分明细列表")
    private IPage<UserScoreDetailsVO> userScoreDetailsVO;
}
