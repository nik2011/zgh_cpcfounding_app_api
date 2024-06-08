package com.yitu.cpcFounding.api.vo.ad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 小程序码
 * @author 蔡李佳
 * @date 2021-01-25 07:21:56
 */
@Data
public class WXCodeVO {
	@ApiModelProperty("标题")
	private String title;
	
	@ApiModelProperty("图片所在路径")
	private String path;
	
	@ApiModelProperty("用户id")
	private String userId;
}
