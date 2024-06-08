package com.yitu.cpcFounding.api.enums;

/**
 * 团队类型枚举
 *
 * @author qixinyi
 * @date 2021/1/26 11:34
 */
public enum TeamTypeEnum {
    AREA(1, "片区"),
    STREET(2, "街道"),
    TEAM(3,"团队");

    private int value;
    private String name;

    TeamTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static TeamTypeEnum fromValue(Integer value) {
        if (value != null) {
            for (TeamTypeEnum teamTypeEnum : TeamTypeEnum.values()) {
                if (teamTypeEnum.getValue() == value) {
                    return teamTypeEnum;
                }
            }
        }
        return null;
    }
}
