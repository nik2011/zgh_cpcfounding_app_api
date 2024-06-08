package com.yitu.cpcFounding.api.enums;

/**
 * 排行榜类型枚举
 *
 * @author qixinyi
 * @date 2021/1/26 11:34
 */
public enum RankingTypeEnum {
    USER_RANKING(1,"用户总榜"),
    USER_WEEK_RANKING(2,"用户周榜"),
    ANSWER_RANKING(3,"答题榜"),
    TEAM_RANKING(4,"团队总榜"),
    TEAM_WEEK_RANKING(5,"团队周榜"),
    STREET_RANKING(6,"街道总榜"),
    STREET_WEEK_RANKING(7,"街道周榜"),
    AREA_RANKING(8,"片区总榜"),
    AREA_WEEK_RANKING(9,"片区周榜");

    private int value;
    private String name;

    RankingTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static RankingTypeEnum fromValue(Integer value) {
        if (value != null) {
            for (RankingTypeEnum rankingTypeEnum : RankingTypeEnum.values()) {
                if (rankingTypeEnum.getValue() == value) {
                    return rankingTypeEnum;
                }
            }
        }
        return null;
    }
}
