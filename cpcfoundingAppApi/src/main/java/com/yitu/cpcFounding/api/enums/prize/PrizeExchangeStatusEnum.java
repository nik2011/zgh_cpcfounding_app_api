package com.yitu.cpcFounding.api.enums.prize;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 奖品兑换码状态枚举
 * @author chenpinjia
 * @version 1.0
 * @date 2021/1/21
 */
@Getter
@AllArgsConstructor
public enum  PrizeExchangeStatusEnum {


    UN_EXCHANGE(0,"未使用"),

    EXCHANGED(1, "已使用"),

    INVALID(2, "已经失效");


    private int code;

    private String name;

    public static String getName(int code) {
        for (PrizeExchangeStatusEnum e : PrizeExchangeStatusEnum.values()) {
            if (e.code == code) {
                return e.name;
            }
        }
        return "";
    }
}
