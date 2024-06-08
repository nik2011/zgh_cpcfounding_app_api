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
public class IndexLsAdVO {
    @ApiModelProperty(value = "首页顶部图")
    private HeadBackPicVO headBackPic;

    @ApiModelProperty(value = "首页广告模块数据")
    private List<ModuleDataListVO> moduleDataLists;
}
