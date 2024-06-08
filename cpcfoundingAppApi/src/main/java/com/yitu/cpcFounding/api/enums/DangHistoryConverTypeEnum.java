package com.yitu.cpcFounding.api.enums;

/**
 * 积分来源类型枚举
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/18 15:56
 */
public enum DangHistoryConverTypeEnum {
    NO_PIC(1, "无图"),
    SINGLE_PIC(2, "单图"),
    THREE_PIC(3, "三图"),
    WIDE_PIC(4, "宽图");

    private int value;
    private String name;

    DangHistoryConverTypeEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static DangHistoryConverTypeEnum fromValue(Integer value) {
        if (value != null) {
            for (DangHistoryConverTypeEnum dangHistoryConverTypeEnum : DangHistoryConverTypeEnum.values()) {
                if (dangHistoryConverTypeEnum.getValue() == value) {
                    return dangHistoryConverTypeEnum;
                }
            }
        }
        return null;
    }
}
