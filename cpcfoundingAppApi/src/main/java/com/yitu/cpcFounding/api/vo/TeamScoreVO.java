package com.yitu.cpcFounding.api.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yitu.cpcFounding.api.vo.team.TeamRankingListVO;
import com.yitu.cpcFounding.api.vo.userScore.UserRankingListVO;
import com.yitu.cpcFounding.api.vo.userScore.UserScoreLabelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 团队排行榜列表以及当前用户所属团队信息
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/8 10:01
 */

@Data
@ApiModel("团队排行榜列表以及当前用户所属团队信息")
public class TeamScoreVO {
    @ApiModelProperty("列表")
    private IPage<TeamRankingListVO> topList;

    @ApiModelProperty("当前登入用户团队信息")
    private TeamRankingListVO self;

    @ApiModelProperty("时间范围(周榜使用)")
    private String time;

    @ApiModelProperty("排行榜提示语")
    private UserScoreLabelVO userScoreLabelVO;
}
