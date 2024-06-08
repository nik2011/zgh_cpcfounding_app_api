package com.yitu.cpcFounding.api.enums;

/**
 * 排行榜类型枚举
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/8 16:11
 */

public enum RankingEnum {
    /**
     * 周榜
     */
    WEEK_RANKING(1, "周榜"),

    /**
     * 总榜
     */
    TOTAL_RANKING(2, "总榜");


    private int value;
    private String name;

    RankingEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static RankingEnum fromValue(Integer value) {
        if (value != null) {
            for (RankingEnum rankingEnum : RankingEnum.values()) {
                if (rankingEnum.getValue() == value) {
                    return rankingEnum;
                }
            }
        }
        return null;
    }
}
