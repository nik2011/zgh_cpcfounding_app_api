package com.yitu.cpcFounding.api.vo.answer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户答题表实体
 * @author shenjun
 * @date 2021-02-24 10:15:22
 */
@Data
@ApiModel("用户答题表实体")
public class LsUserFinishAnswerVO {
    /**
    * 答题正确数量
    */
    @ApiModelProperty("答题正确数量")
    private long answerCount;

    /**
     * 增加积分数
     */
    @ApiModelProperty("答题正确数量")
    private long addScore;

    /**
    * 是否达到积分上限
    */
    @ApiModelProperty("是否达到积分上限")
    private boolean overScroe;



}
