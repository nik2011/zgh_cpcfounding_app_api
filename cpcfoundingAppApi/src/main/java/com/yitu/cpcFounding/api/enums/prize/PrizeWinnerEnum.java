package com.yitu.cpcFounding.api.enums.prize;

import com.baomidou.mybatisplus.core.enums.IEnum;

/**
 * @Auther: zhangyongfeng
 * @Date: 2021/1/26 11 : 11
 * @Description:
 */
public enum PrizeWinnerEnum implements IEnum<Integer> {

//    HOT(0, "暖城"),
    YEAR_PRAISE(1, "晒年味");

    private int value;
    private String name;

    PrizeWinnerEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static PrizeWinnerEnum fromValue(Integer value) {
        if (value != null) {
            for (PrizeWinnerEnum prizeWinnerEnum : PrizeWinnerEnum.values()) {
                if (prizeWinnerEnum.getValue() == value) {
                    return prizeWinnerEnum;
                }
            }
        }
        return null;
    }

}
