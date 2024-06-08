package com.yitu.cpcFounding.api.vo.team;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 团队信息以及当前用户所属团队信息
 * @author wangping
 * @version 1.0
 * @date 2021/04/09
 */
@Data
@ApiModel("团队信息以及当前用户所属团队信息")
public class UserTeamListVO {

    @ApiModelProperty("团队列表")
    IPage<TeamInfoVO> topList;

    @ApiModelProperty("当前登入用户团队信息")
    TeamInfoVO self;

    @ApiModelProperty("当前用户团队状态 true已加入，false未加入")
    private  Boolean userStatus;

}
