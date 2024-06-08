package com.yitu.cpcFounding.api.enums;

/**
 * TODO
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/22 16:06
 */

public enum AdShareTypeEnum {
    SHARE_QUESTION(22, "答题的分享"),
    SHARE_INDEX(23, "首页分享"),
    ARTICLE_SHARE(73,"文章分享"),
    RANKING_SHARE(74,"排行榜分享"),
    TASK_SHARE(75,"任务分享"),
    ROTARY_DRAW_SHARE(76,"抽奖分享"),
    TEAM_SHARE(77,"团队分享");

    private int value;
    private String name;

    AdShareTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static AdShareTypeEnum fromValue(Integer value) {
        if (value != null) {
            for (AdShareTypeEnum adShareTypeEnum : AdShareTypeEnum.values()) {
                if (adShareTypeEnum.getValue() == value) {
                    return adShareTypeEnum;
                }
            }
        }
        return null;
    }
}
