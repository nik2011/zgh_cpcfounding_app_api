package com.yitu.cpcFounding.api.enums;

/**
 * 微信审核枚举
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/5/26 16:51
 */

public enum WxSecCheckEnum {
    UNPASS_ERRCODE(87014, "审核不通过"),
    PASS_ERRCODE(0,"审核通过"),
    TOKEN_ERRCODE(40001,"token失效"),
    TOKEN_SUCCESS_ERRCODE(0,"token获取成功");

    private int value;
    private String name;

    WxSecCheckEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static WxSecCheckEnum fromValue(Integer value) {
        if (value != null) {
            for (WxSecCheckEnum wxSecCheckEnum: WxSecCheckEnum.values()) {
                if (wxSecCheckEnum.getValue() == value) {
                    return wxSecCheckEnum;
                }
            }
        }
        return null;
    }
}
