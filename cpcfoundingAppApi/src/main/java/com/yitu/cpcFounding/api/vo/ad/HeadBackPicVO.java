package com.yitu.cpcFounding.api.vo.ad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * 首页顶部背景图
 *
 * @author jxc
 * @date 2021/1/21
 */
@Data
public class HeadBackPicVO {
    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String picPath;
}
