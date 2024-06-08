package com.yitu.cpcFounding.api.enums.prize;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 工会奖品类型（位与）
 * @author chenpinjia
 * @version 1.0
 * @date 2021/1/21
 */
@Getter
@AllArgsConstructor
public enum PrizeMemberTypeEnum {

    NORMAL(1,"普通会员"),

    UNION(2,"工会会员");


    private int code;

    private String name;

    public static String getName(int code) {
        for (PrizeMemberTypeEnum e : PrizeMemberTypeEnum.values()) {
            if (e.code == code) {
                return e.name;
            }
        }
        return "";
    }
}
