package com.yitu.cpcFounding.api.vo.team;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 团队排行榜
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/8 17:24
 */

@Data
@ApiModel("团队排行榜")
public class TeamRankingListVO {
    /**
     * 团队id
     */
    @ApiModelProperty("团队id")
    private Long id;

    /**
     * 团队名称
     */
    @ApiModelProperty("团队名称")
    private String name;

    /**
     * 积分总数
     */
    @ApiModelProperty("积分总数")
    private int scoreTotal;

    /**
     * 当前团队所在排行
     */
    @ApiModelProperty(value = "当前团队所在排行")
    private Integer teamSort;
}
