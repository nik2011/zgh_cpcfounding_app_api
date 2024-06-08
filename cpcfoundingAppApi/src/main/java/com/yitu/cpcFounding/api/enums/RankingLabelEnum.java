package com.yitu.cpcFounding.api.enums;


/**
 *
 * 排行榜提示语类型枚举
 *
 @author wangping
 * @date 2021/6/28 15:54
 */

public enum RankingLabelEnum {
    RANKING_LABEL_1(1, "个人周榜"),
    RANKING_LABEL_2(2, "个人总榜"),
    RANKING_LABEL_3(3, "团队周榜（企业）"),
    RANKING_LABEL_4(4, "团队周榜（街道）"),
    RANKING_LABEL_5(5, "团队周榜（片区）"),
    RANKING_LABEL_6(6, "团队总榜（企业）"),
    RANKING_LABEL_7(7, "团队总榜（街道）"),
    RANKING_LABEL_8(8, "团队总榜（片区）"),
    RANKING_LABEL_9(9, "答题总榜"),
    ;


    private int value;
    private String name;

    RankingLabelEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static RankingLabelEnum fromValue(Integer value) {
        if (value != null) {
            for (RankingLabelEnum rankingEnum : RankingLabelEnum.values()) {
                if (rankingEnum.getValue() == value) {
                    return rankingEnum;
                }
            }
        }
        return null;
    }

    public static RankingLabelEnum parse(int type) {
        for (RankingLabelEnum item : RankingLabelEnum.values()) {
            if (item.getValue() == type) {
                return item;
            }
        }
        return null;
    }
}
