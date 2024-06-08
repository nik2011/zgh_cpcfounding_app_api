package com.yitu.cpcFounding.api.enums.prize;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 奖品类型枚举
 *
 * @author chenpinjia
 * @version 1.0
 * @date 2021/1/21
 */
@Getter
@AllArgsConstructor
public enum PrizeTypeEnum {

    MATERIAL(1, "实物"),

    VIRTUAL(2, "虚拟"),

    CASH(3, "现金"),

    HOTEL(4, "酒店"),

    ADD_COUNT(5, "次数增加"),

    NOTHING(6, "什么都没有"),

    VOUCHER(7,"代金券"),

    VIRTUAL_MEDAL(8, "虚拟奖章"),

    SCORE(9, "积分");

    private int code;

    private String name;

    public static String getName(int code) {
        for (PrizeTypeEnum e : PrizeTypeEnum.values()) {
            if (e.code == code) {
                return e.name;
            }
        }
        return "";
    }


}
