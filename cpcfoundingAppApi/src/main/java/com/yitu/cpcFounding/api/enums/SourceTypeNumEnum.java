package com.yitu.cpcFounding.api.enums;

/**
 * 积分各类型加入数量枚举及上限分数
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/11 11:15
 */

public enum SourceTypeNumEnum {
    SIGIN_IN(1, 5),//签到5分
    READ_ARTICLE(2, 5),//阅读文章5分
    DAY_ANSWER(3, 5),//每日答题
    DAY_FIRST_SHARE(4, 5),//每日首次分享
    INVITE_FRIEND(5, 20),//邀请好友完成答题
    REWARD(6, 10),//奖励得分
    JOIN_TEAM(7, 50),//加入团队
    //8.转盘抽奖；9.企业（工联会）周榜排名奖励；10.街道级工会周榜排名奖励;11.区级工会周榜排名奖励
    ROTARY_DRAW(8, 100),//转盘抽奖
    COMPANY_REWARD(9, 100),//企业（工联会）周榜排名奖励
    STREET_REWARD(10, 100),//街道级工会周榜排名奖励
    AREA_REWARD(11, 100),//区级工会周榜排名奖励
    //各类型积分每日上限数量
    SIGN_IN_COUNT(12,5),//签到上限5分
    READ_ARTICLE_COUNT(13,50),//阅读上限50分
    DAY_ANSWER_COUNT(14,500),//每日答题上限500分
    DAY_FIRST_SHARE_COUNT(15,5),//分享上限5分
    INVITE_FRIEND_COUNT(16,100),//邀请好友上限100分

    //各类型积分每日上限次数
    SIGN_IN_TIME(121,1),//签到上限次数1次
    READ_ARTICLE_TIME(131,10),//阅读上限次数10次
    DAY_ANSWER_TIME(141,10),//每日答题上限次数100次
    DAY_FIRST_SHARE_TIME(151,1),//分享上限次数1次
    INVITE_FRIEND_TIME(161,5),//邀请好友上限次数5次
    JOIN_TEAM_TIME(171,1)//加入团队上限次数1次
    ;

    private int value;
    private Integer name;

    SourceTypeNumEnum(int value, int name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public Integer getName() {
        return name;
    }

    public static Integer fromValue(Integer value) {
        if (value != null) {
            for (SourceTypeNumEnum scoreTypeEnum : SourceTypeNumEnum.values()) {
                if (scoreTypeEnum.getValue() == value) {
                    return scoreTypeEnum.getName();
                }
            }
        }
        return null;
    }
}
