package com.yitu.cpcFounding.api.vo.userScore;

import com.yitu.cpcFounding.api.enums.SourceTypeNumEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户每日积分明细实体
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/11 16:15
 */

@Data
@ApiModel("用户每日积分明细实体对象")
public class UserScoresOfDay {
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("签到积分")
    private Integer signInScore;

    @ApiModelProperty("签到积分每日上限")
    private Integer limitSignInScore= SourceTypeNumEnum.SIGN_IN_COUNT.getName();

    @ApiModelProperty("阅读文章积分")
    private Integer readArticleScore;

    @ApiModelProperty("阅读文章积分每日上限")
    private Integer limitReadArticleScore= SourceTypeNumEnum.READ_ARTICLE_COUNT.getName();

    @ApiModelProperty("每日答题积分")
    private Integer answerScore;

    @ApiModelProperty("每日答题积分上限")
    private Integer limitAnswerScore= SourceTypeNumEnum.DAY_ANSWER_COUNT.getName();

    @ApiModelProperty("分享积分")
    private Integer shareScore;

    @ApiModelProperty("分享积分上限")
    private Integer limitShareScore= SourceTypeNumEnum.DAY_FIRST_SHARE_COUNT.getName();

    @ApiModelProperty("邀请好友积分")
    private Integer inviteScore;

    @ApiModelProperty("邀请好友积分上限")
    private Integer limitInviteScore= SourceTypeNumEnum.INVITE_FRIEND_COUNT.getName();

    @ApiModelProperty("加入团队积分上限")
    private Integer limitTeamScore= SourceTypeNumEnum.JOIN_TEAM.getName();

    @ApiModelProperty("当前用户团队积分")
    private  Integer userTeamScore;
}
