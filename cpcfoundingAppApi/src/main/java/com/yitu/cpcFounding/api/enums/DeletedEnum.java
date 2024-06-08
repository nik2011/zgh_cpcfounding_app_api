package com.yitu.cpcFounding.api.enums;

/**
 * 删除枚举
 * @author shenjun
 * @date 2021/1/21 17:10
 */
public enum DeletedEnum {
    /**
     *  未删除
     */
    NOT_DELETE(0, "未删除"),
    DELETE(1, "已删除");
    private int value;
    private String name;

    DeletedEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static DeletedEnum fromValue(Integer value) {
        if (value != null) {
            for (DeletedEnum deletedEnum : DeletedEnum.values()) {
                if (deletedEnum.getValue() == value) {
                    return deletedEnum;
                }
            }
        }
        return null;
    }
}
