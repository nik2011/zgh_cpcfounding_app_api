package com.yitu.cpcFounding.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 会员状态
 *
 * @author qinmingtong
 * @date 2021/1/23 11:15
 */
@Getter
@AllArgsConstructor
public enum MemberStateEnum {

    /**
     * 不是会员
     */
    YEAR_FESTIVAL(0, "不是会员"),

    /**
     * 工会会员
     */
    RULES(1, "工会会员");


    private int code;

    private String name;


    public static String getName(int code) {
        for (MemberStateEnum c : MemberStateEnum.values()) {
            if (c.code == code) {
                return c.name;
            }
        }
        return null;
    }

    public static MemberStateEnum getEnumByCode(int code) {
        for (MemberStateEnum type : MemberStateEnum.values()) {
            if (code == type.code) {
                return type;
            }
        }
        return null;
    }
}

