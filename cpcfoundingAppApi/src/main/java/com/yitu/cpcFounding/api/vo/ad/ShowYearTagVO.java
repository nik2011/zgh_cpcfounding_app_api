package com.yitu.cpcFounding.api.vo.ad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * TODO
 *
 * @author jxc
 * @date 2021/1/23
 */
@Data
public class ShowYearTagVO {
    public ShowYearTagVO() {
    }

    public ShowYearTagVO(String type, String name) {
        this.type = type;
        this.name = name;
    }

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("类型字符串")
    private String name;
}
