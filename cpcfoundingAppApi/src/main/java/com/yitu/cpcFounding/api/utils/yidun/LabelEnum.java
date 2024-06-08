package com.yitu.cpcFounding.api.utils.yidun;

/**
 * 分类信息
 * @author shenjun
 * @date 2021/1/23 10:13
 */
public enum LabelEnum {
    /**
     *  色情
     */
    PORN(100,"色情"),
    AD(200,"广告"),
    AD_LAW(260,"广告法"),
    FEAR(300,"暴恐"),
    PROHIBITED(400,"违禁"),
    POLITICS(500,"涉政"),
    RRIGATION(600,"谩骂"),
    WATER(700,"灌水"),
    OTHER(900,"其他"),
    VALUES(1100,"涉价值观");

    private int value;
    private String name;

    LabelEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static LabelEnum fromValue(Integer value) {
        if (value != null) {
            for (LabelEnum labelEnum : LabelEnum.values()) {
                if (labelEnum.getValue() == value) {
                    return labelEnum;
                }
            }
        }
        return null;
    }
}
