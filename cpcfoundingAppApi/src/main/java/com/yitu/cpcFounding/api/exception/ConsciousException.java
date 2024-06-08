package com.yitu.cpcFounding.api.exception;


import com.yitu.cpcFounding.api.enums.ResponseCode;

/**
 * 业务主动异常 
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/11
 */
public class ConsciousException extends RuntimeException {

    private int code;

    public ConsciousException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public ConsciousException(String msg) {
        super(msg);
        this.code = ResponseCode.INTERNAL_SERVER_ERROR.getCode();
    }

    public ConsciousException(ResponseCode responseCode, String msg) {
        super(msg);
        this.code = responseCode.getCode();
    }

    public ConsciousException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.code = responseCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
