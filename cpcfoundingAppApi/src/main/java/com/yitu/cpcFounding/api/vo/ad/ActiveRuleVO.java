package com.yitu.cpcFounding.api.vo.ad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author jxc
 * @date 2021/1/21
 */
@Data
public class ActiveRuleVO {
    /**
     * 活动规则
     */
    @ApiModelProperty(value = "活动规则")
    private String rule;
}
