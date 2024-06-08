package com.yitu.cpcFounding.api.vo.ad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 广告信息
 * @author jxc
 * @date 2021-01-21 11:02:18
 */
@Data
public class LsAdVO {
    /**
    * 主键
    */
    @ApiModelProperty(value = "主键")
    private Long id;

    /**
     * 广告类型：1banner图2福利活动3视屏广告4惠服务5深圳工会
     */
    @ApiModelProperty(value = "广告类型：1banner图2福利活动3视屏广告4惠服务5深圳工会")
    private Integer type;

    /**
    * 标题
    */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
    * 子标题
    */
    @ApiModelProperty(value = "子标题")
    private String subTitle;

    /**
    * 图片地址
    */
    @ApiModelProperty(value = "图片地址")
    private String picPath;

    /**
    * 内容
    */
    @ApiModelProperty(value = "内容")
    private String content;

    /**
    * 链接类型（1内链2外链）
    */
    @ApiModelProperty(value = "链接类型（1内链2外链）")
    private Integer linkType;

    /**
    * 链接（跳转）地址
    */
    @ApiModelProperty(value = "链接（跳转）地址")
    private String linkUrl;
}