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
@ApiModel("用户积分实体对象")
public class UserScoreTotalVO {
    @ApiModelProperty("用户id")
    private Integer id;

    @ApiModelProperty("微信头像")
    private String wxHeadPic;

    @ApiModelProperty("积分总数")
    private int scoreTotal;

    @ApiModelProperty("是否加入团队")
    private Boolean isJoinTeam;

    @ApiModelProperty("团队名")
    private String teamName;
}
