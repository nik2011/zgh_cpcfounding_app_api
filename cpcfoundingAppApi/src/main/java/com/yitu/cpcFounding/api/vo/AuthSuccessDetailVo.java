package com.yitu.cpcFounding.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 认证成功详情
 *
 * @author jxc
 * @date 2021/1/26
 */
@Data
public class AuthSuccessDetailVo {
    @ApiModelProperty(value = "认证工会手机号")
    private String phone;

    @ApiModelProperty(value = "认证状态：会员状态 1 工会会员 0不是会员")
    private Integer memberState;

    @ApiModelProperty(value = "认证结果文字信息")
    private String tip;
}
