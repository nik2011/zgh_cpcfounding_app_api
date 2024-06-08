package com.yitu.cpcFounding.api.vo.userScore;

import com.yitu.cpcFounding.api.enums.SourceTypeNumEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户每日次数实体类
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/7/2 15:31
 */

@Data
@ApiModel("用户每日次数实体对象")
public class UserScoreTimesOfDay {
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("签到次数")
    private Integer signInTimes;

    @ApiModelProperty("签到次数每日上限")
    private Integer limitSignInTimes= SourceTypeNumEnum.SIGN_IN_TIME.getName();

    @ApiModelProperty("阅读文章次数")
    private Integer readArticleTimes;

    @ApiModelProperty("阅读文章次数每日上限")
    private Integer limitReadArticleTimes= SourceTypeNumEnum.READ_ARTICLE_TIME.getName();

    @ApiModelProperty("每日答题次数")
    private Integer answerTimes;

    @ApiModelProperty("每日答题次数上限")
    private Integer limitAnswerTimes= SourceTypeNumEnum.DAY_ANSWER_TIME.getName();

    @ApiModelProperty("分享次数")
    private Integer shareTimes;

    @ApiModelProperty("分享次数上限")
    private Integer limitShareTimes= SourceTypeNumEnum.DAY_FIRST_SHARE_TIME.getName();

    @ApiModelProperty("邀请好友次数")
    private Integer inviteTimes;

    @ApiModelProperty("邀请好友次数上限")
    private Integer limitInviteTimes= SourceTypeNumEnum.INVITE_FRIEND_TIME.getName();

    @ApiModelProperty("当前用户团队次数")
    private  Integer userTeamTimes;

    @ApiModelProperty("当前团队次数上限")
    private Integer limitTeamTimes=SourceTypeNumEnum.JOIN_TEAM_TIME.getName();
}
