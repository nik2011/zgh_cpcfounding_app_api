package com.yitu.cpcFounding.api.vo.ad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 首页广告信息
 *
 * @author jxc
 * @date 2021/1/21
 */
@Data
public class ModuleDataListVO {

    @ApiModelProperty(value = "模块类型")
    private Integer type;

    @ApiModelProperty(value = "模块描述")
    private String title;

    @ApiModelProperty(value = "项")
    private List<LsAdVO> items;
}
