package com.yitu.cpcFounding.api.enums.prize;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 摇一摇日志类型
 * @author chenpinjia
 * @version 1.0
 * @date 2021/1/21
 */
@Getter
@AllArgsConstructor
public enum ShakeTakeLogTypeEnum {

    SHAKE(1,"摇一摇"),

    SHARE(2,"分享");

    private int code;

    private String name;

    public static String getName(int code) {
        for (ShakeTakeLogTypeEnum e : ShakeTakeLogTypeEnum.values()) {
            if (e.code == code) {
                return e.name;
            }
        }
        return "";
    }

}
