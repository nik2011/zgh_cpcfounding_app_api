package com.yitu.cpcFounding.api.vo.team;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yitu.cpcFounding.api.vo.userScore.UserRankingListVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 团队信息以及团队下用户列表（分页）
 * @author wangping
 * @version 1.0
 * @date 2021/04/09
 */
@Data
@ApiModel("团队信息以及团队下用户列表")
public class TeamUserListVO {

    @ApiModelProperty("用户列表")
    private IPage<UserRankingListVO> topList;

    @ApiModelProperty("团队信息")
    private TeamVO self;

    @ApiModelProperty("当前用户团队状态 true已加入，false未加入")
    private  Boolean userStatus;

}
