package com.yitu.cpcFounding.api.enums;

/**
 * 响应码枚举
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/11
 */
public enum ResponseCode {
    /** 成功 */
    OK(200, "成功"),
    /** 用户身份认证失败 */
    UNAUTHORIZED(401, "用户身份认证失败"),
    /** 没有权限 */
    NOT_PERMISSION(402, "没有权限"),
    /** 拒绝请求 */
    FORBIDDEN(403, "拒绝请求"),
    /** 未找到资源 */
    NOT_FOUND(404, "未找到资源"),
    /** 服务器内部错误 */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    /** 请求参数验证失败 */
    UNVALIDATED(406, "请求参数验证失败");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
