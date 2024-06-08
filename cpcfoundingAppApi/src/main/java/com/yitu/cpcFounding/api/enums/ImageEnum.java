package com.yitu.cpcFounding.api.enums;

/**
 * 图片枚举
 * @author shenjun
 * @date 2021/1/27 10:15
 */
public enum ImageEnum {
    //列表图
    SMALL(320, "small"),
    //详情图
    MEDIUM(480, "medium");
    private int value;
    private String name;

    ImageEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static ImageEnum fromValue(Integer value) {
        if (value != null) {
            for (ImageEnum imageEnum : ImageEnum.values()) {
                if (imageEnum.getValue() == value) {
                    return imageEnum;
                }
            }
        }
        return null;
    }
}
