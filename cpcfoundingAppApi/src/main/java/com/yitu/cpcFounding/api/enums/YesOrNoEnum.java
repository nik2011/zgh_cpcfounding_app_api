package com.yitu.cpcFounding.api.enums;

import lombok.Getter;

/**
 * 是否通用枚举
 *
 * @author qinmingtong
 * @date 2021/1/26 11:34
 */
@Getter
public enum YesOrNoEnum {

    /**
     * 否
     */
    NO(0, "否"),

    /**
     * 是
     */
    YES(1, "是");

    /**
     * 枚举值
     */
    private final int value;

    /**
     * 名称
     */
    private final String name;

    /**
     * 构造方法
     *
     * @param value 枚举值
     * @param name  名称
     * @author qinmingtong
     * @date 2021/1/26 11:47
     */
    YesOrNoEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    /**
     * 根据枚举值获取名称
     *
     * @param value 枚举值
     * @return YesOrNoEnum
     * @author qinmingtong
     * @date 2021/1/26 11:38
     */
    public static YesOrNoEnum fromValue(Integer value) {
        if (value != null) {
            for (YesOrNoEnum yesOrNoEnum : YesOrNoEnum.values()) {
                if (yesOrNoEnum.getValue() == value) {
                    return yesOrNoEnum;
                }
            }
        }
        return null;
    }

}
