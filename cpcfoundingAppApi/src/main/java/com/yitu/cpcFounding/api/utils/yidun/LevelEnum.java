package com.yitu.cpcFounding.api.utils.yidun;

/**
 * 分类枚举
 * @author shenjun
 * @date 2021/1/23 10:19
 */
public enum LevelEnum {
    /**
     *  通过
     */
    PASS(0,"通过"),
    SUSPECT(1,"嫌疑"),
    NOT_PASS(2,"不通过");

    private int value;
    private String name;

    LevelEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static LevelEnum fromValue(Integer value) {
        if (value != null) {
            for (LevelEnum levelEnum : LevelEnum.values()) {
                if (levelEnum.getValue() == value) {
                    return levelEnum;
                }
            }
        }
        return null;
    }
}
