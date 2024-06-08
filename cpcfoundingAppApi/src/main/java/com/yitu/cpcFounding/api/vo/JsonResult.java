package com.yitu.cpcFounding.api.vo;

import com.yitu.cpcFounding.api.enums.ResponseCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;


/**
 * 统一响应结构
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/11
 */
@ApiModel("api接口通用返回对象")
public class JsonResult<T> implements Serializable {

    //状态码
    @ApiModelProperty(value = "业务接口响应状态码", example = "200")
    private Integer status;
    //描述
    @ApiModelProperty(value = "业务接口响应描述")
    private String msg;
    //响应数据
    @ApiModelProperty(value = "业务接口响应数据")
    private T data;

    public JsonResult(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static <T> JsonResult<T> build(Integer status, String msg, T data) {
        return new JsonResult<>(status, msg, data);
    }

    public static <T> JsonResult<T> build(Integer status, String msg) {
        return new JsonResult<>(status, msg, null);
    }

    public static <T> JsonResult<T> build(ResponseCode code) {
        return new JsonResult<>(code.getCode(), code.getMessage(), null);
    }

    public static <T> JsonResult<T> build(ResponseCode code, String msg) {
        return new JsonResult<>(code.getCode(), msg, null);
    }

    public static <T> JsonResult<T> build(ResponseCode code, String msg, T data) {
        return new JsonResult<>(code.getCode(), msg, data);
    }

    public static <T> JsonResult<T> ok() {
        return JsonResult.build(ResponseCode.OK);
    }

    public static <T> JsonResult<T> ok(T data) {
        return JsonResult.build(ResponseCode.OK, ResponseCode.OK.getMessage(), data);
    }

    public static <T> JsonResult<T> ok(String msg, T data) {
        return build(ResponseCode.OK, msg, data);
    }

    public static <T> JsonResult<T> fail(String msg) {
        return new JsonResult<>(ResponseCode.INTERNAL_SERVER_ERROR.getCode(), StringUtils.isBlank(msg) ? "未知异常" : msg,
                null);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
