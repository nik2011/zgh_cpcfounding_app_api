package com.yitu.cpcFounding.api.vo.userScore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户积分实体
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/10 16:08
 */

@Data
@ApiModel("排行榜提示信息")
public class UserScoreLabelVO {
    /**
     * 状态 0 不跳转 1 跳我的奖品 2 跳积分明细
     */
    @ApiModelProperty(" 状态 0 不跳转 1 跳我的奖品 2 跳积分明细")
    private Integer type;

    /**
     * 提示语
     */
    @ApiModelProperty("排行榜提示语")
    private String title;
}
