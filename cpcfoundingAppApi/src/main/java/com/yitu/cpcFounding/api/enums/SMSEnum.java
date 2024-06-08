package com.yitu.cpcFounding.api.enums;

/**
 *
 * 2021/1/25 18:36
 * 发送短信响应码
 *
 * @author luzhichao
 **/
public enum SMSEnum {

    SUCCESS(100, "未删除"),
    FAIL(101, "失败！"),
    VERIFICAT_FAIL(102, "校验错误"),
    PHONE_ERROR(103, "号码有错"),
    CONTENT_ERROR(104, "内容错误"),
    FREQUENTLY(105, "操作频率过快"),
    Limit_send(106, "限制发送"),
    param_error(107, "参数不全");

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    SMSEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static SMSEnum getEnum(Integer code) {
        if (code != null) {
            for (SMSEnum smsEnum : SMSEnum.values()) {
                if (smsEnum.getCode() == code) {
                    return smsEnum;
                }
            }
        }
        return null;
    }
}
