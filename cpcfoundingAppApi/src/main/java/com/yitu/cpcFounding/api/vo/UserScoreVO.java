package com.yitu.cpcFounding.api.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yitu.cpcFounding.api.vo.userScore.UserRankingListVO;
import com.yitu.cpcFounding.api.vo.userScore.UserScoreLabelVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 个人排行榜列表以及当前用户所属信息
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/8 15:44
 */

@Data
@ApiModel("个人排行榜列表以及当前用户所属信息")
public class UserScoreVO {
    @ApiModelProperty("列表")
    private IPage<UserRankingListVO> topList;

    @ApiModelProperty("当前登入用户信息")
    private UserRankingListVO self;

    @ApiModelProperty("时间范围(周榜使用)")
    private String time;

    @ApiModelProperty("排行榜提示语")
    private UserScoreLabelVO userScoreLabelVO;

}
