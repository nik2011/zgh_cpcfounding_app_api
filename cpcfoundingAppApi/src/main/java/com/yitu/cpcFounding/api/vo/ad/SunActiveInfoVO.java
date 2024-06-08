package com.yitu.cpcFounding.api.vo.ad;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 晒活动页面信息
 *
 * @author jxc
 * @date 2021/1/21
 */
@Data
public class SunActiveInfoVO {
    /**
     *  获取晒年味头图广告
     */
    @ApiModelProperty(value = "晒活动头图广告")
    private HeadPicIsadVO headPicIsad;

    @ApiModelProperty(value = "晒活动中间广告")
    private HeadPicIsadVO middlePicIsad;

    /**
     * 作品数量
     */
    @ApiModelProperty(value = "作品数量")
    private Long partakeUserNum;

    /**
     * 束花数量
     */
    @ApiModelProperty(value = "束花数量")
    private Long likeNum;

    /**
     * 是否显示获奖名单
     */
    @ApiModelProperty(value = "是否显示获奖名单:0否1是")
    private Integer isShowYearPrizeWinner;

    /**
     * 主贴列表头部标签
     */
    //@ApiModelProperty(value = "主贴列表头部标签")
    //private List<ShowYearTagVO> headTags;

    /**
     * 活动是否开始
     */
    @ApiModelProperty(value = "活动是否开始")
    private boolean activeRunning;
}
