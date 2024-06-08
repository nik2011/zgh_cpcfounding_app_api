package com.yitu.cpcFounding.api.enums;

/**
 * 会员状态
 *
 * @author yaoyanhua
 * @version 1.0
 * @date 2021/01/21
 */
public enum MemberState {
    Common(0, "普通会员"),
    Member(1, "工会会员");

    private final int mCode;
    private final String mText;

    MemberState(int code, String text) {
        mCode = code;
        mText = text;
    }

    public int getCode() {
        return mCode;
    }

    public String getText() {
        return mText;
    }
}
