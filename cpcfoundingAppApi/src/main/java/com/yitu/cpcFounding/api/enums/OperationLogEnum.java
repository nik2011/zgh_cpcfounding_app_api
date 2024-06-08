package com.yitu.cpcFounding.api.enums;

import lombok.Getter;

/**
 * 操作类型枚举
 *
 * @author qinmingtong
 * @date 2021/1/27 10:04
 */
@Getter
public enum OperationLogEnum {

    /** 501~600 小程序-摇一摇 **/




    /** 601~700 小程序-晒年味 **/




    /** 701~800 小程序-热力 **/

    SEND_VOUCHER(5001,"发放代金券");
    /**
     * 操作类型code
     */
    private final int code;

    /**
     * 操作类型说明
     */
    private final String name;

    OperationLogEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * 通过操作类型code获取操作类型枚举
     *
     * @param code 操作类型code
     * @return com.yitu.year.adminweb.enums.OperationLogEnum
     * @author qinmingtong
     * @date 2021/1/27 10:13
     */
    public static OperationLogEnum fromCode(Integer code) {
        if (code != null) {
            for (OperationLogEnum operationLogEnum : OperationLogEnum.values()) {
                if (operationLogEnum.getCode() == code) {
                    return operationLogEnum;
                }
            }
        }
        return null;
    }

}
