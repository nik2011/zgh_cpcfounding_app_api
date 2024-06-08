package com.yitu.cpcFounding.api.vo.wxCode;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 小程序码
 * @author 蔡李佳
 * @date 2021-01-25 04:55:08
 */
@Data
public class CodeBean {
	@ApiModelProperty("token")
	private String access_token;
	
	@ApiModelProperty("小程序码传递参数")
	private String scene;
	
	@ApiModelProperty("跳转页")
	private String page;
}
