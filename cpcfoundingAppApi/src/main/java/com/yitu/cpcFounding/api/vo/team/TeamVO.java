package com.yitu.cpcFounding.api.vo.team;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 团队信息vo
 * @author wangping
 * @version 1.0
 * @date 2021/04/09
 */
@Data
@ApiModel("团队信息")
public class TeamVO {

    @ApiModelProperty(value = "团队id")
    @JsonSerialize(using = ToStringSerializer.class)
    private long id;

    @ApiModelProperty(value = "团队名称")
    private String name;

    /**j积分数*/
    @ApiModelProperty(value = "积分数")
    private Long scoreTotal;

    /**总人数*/
    @ApiModelProperty(value = "总人数")
    private Long peopleTotal;

    /**
     * 团队获奖状态 0未获奖、1已获奖
     */
    @ApiModelProperty("团队获奖状态 0未获奖、1已获奖" )
    private int prizeStatus;

    /**
     * 当前用户是否处于该团队状态 0不属于、1属于
     */
    @ApiModelProperty("当前用户是否处于该团队状态 0不属于、1属于" )
    private int userTeamStatus;
}
