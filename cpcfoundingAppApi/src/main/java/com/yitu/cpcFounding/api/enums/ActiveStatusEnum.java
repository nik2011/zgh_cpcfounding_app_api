package com.yitu.cpcFounding.api.enums;

/**
 * 活动状态
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/28 14:37
 */

public enum ActiveStatusEnum {
    BEGIN(0, "未开始"),
    ING(1, "活动中"),
    END(2,"已结束");

    private int value;
    private String name;

    ActiveStatusEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static ActiveStatusEnum fromValue(Integer value) {
        if (value != null) {
            for (ActiveStatusEnum activeStatusEnum : ActiveStatusEnum.values()) {
                if (activeStatusEnum.getValue() == value) {
                    return activeStatusEnum;
                }
            }
        }
        return null;
    }
}
