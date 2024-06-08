package com.yitu.cpcFounding.api.vo.ad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 活动规则
 * 2021/1/29 10:15
 * @author luzhichao
 **/
@Data
public class ActiveRuleInfoVO {

    @ApiModelProperty(value = "内容链接")
    private String contentUrl;
}
