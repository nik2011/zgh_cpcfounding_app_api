package com.yitu.cpcFounding.api.enums.prize;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户奖品状态枚举
 * @author chenpinjia
 * @version 1.0
 * @date 2021/1/21
 */
@Getter
@AllArgsConstructor
public enum  UserPrizeStatusEnum {

    UN_EXCHANGE(0,"未兑换"),

    EXCHANGED(1, "已兑换"),

    SEND(2, "已发货");


    private int code;

    private String name;

    public static String getName(int code) {
        for (UserPrizeStatusEnum e : UserPrizeStatusEnum.values()) {
            if (e.code == code) {
                return e.name;
            }
        }
        return "";
    }
}
