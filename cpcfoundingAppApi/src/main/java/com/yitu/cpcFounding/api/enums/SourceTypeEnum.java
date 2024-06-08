package com.yitu.cpcFounding.api.enums;

/**
 * 积分来源类型枚举
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/11 11:15
 */

public enum SourceTypeEnum {
    SIGIN_IN(1, "签到"),
    READ_ARTICLE(2, "阅读文章"),
    DAY_ANSWER(3, "每日答题"),
    DAY_FIRST_SHARE(4, "每日首次分享"),
    INVITE_FRIEND(5, "邀请好友完成答题"),
    REWARD(6, "奖励得分"),
    JOIN_TEAM(7, "加入团队"),
    //8.转盘抽奖；9.企业（工联会）周榜排名奖励；10.街道级工会周榜排名奖励;11.区级工会周榜排名奖励
    ROTARY_DRAW(8,"转盘抽奖"),
    COMPANY_REWARD(9,"企业（工联会）周榜排名奖励"),
    STREET_REWARD(10,"街道级工会周榜排名奖励"),
    AREA_REWARD(11,"区级工会周榜排名奖励");

    private int value;
    private String name;

    SourceTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static SourceTypeEnum fromValue(Integer value) {
        if (value != null) {
            for (SourceTypeEnum scoreTypeEnum : SourceTypeEnum.values()) {
                if (scoreTypeEnum.getValue() == value) {
                    return scoreTypeEnum;
                }
            }
        }
        return null;
    }
}
