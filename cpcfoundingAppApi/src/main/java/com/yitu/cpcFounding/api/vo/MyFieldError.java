package com.yitu.cpcFounding.api.vo;

public class MyFieldError {
    /**
     * id
     */
    private String field;
    /**
     * 默认信息
     */
    private String defaultMessage;

    public MyFieldError(String field, String defaultMessage){
        this.field = field;
        this.defaultMessage = defaultMessage;
    }
    public MyFieldError(){}

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public void setDefaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }
}

