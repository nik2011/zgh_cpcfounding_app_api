package com.yitu.cpcFounding.api.vo.ad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 晒年味头图广告+获取用户数量+点赞数量+标签
 *
 * @author jxc
 * @date 2021/1/21
 */
@Data
public class HeadPicIsadVO {

    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String picPath;
    /**
     * 链接类型
     */
    @ApiModelProperty(value = "链接类型")
    private Integer linkType;
    /**
     * 链接（跳转）地址
     */
    @ApiModelProperty(value = "链接（跳转）地址")
    private String linkUrl;

}
