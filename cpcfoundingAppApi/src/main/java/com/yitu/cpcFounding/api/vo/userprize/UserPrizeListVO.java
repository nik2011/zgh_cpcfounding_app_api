package com.yitu.cpcFounding.api.vo.userprize;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 我的奖品列表Vo
 * @author chenpinjia
 * @version 1.0
 * @date 2021/1/25
 */
@Data
@ApiModel("用户奖品列表VO_2")
public class UserPrizeListVO {

    @ApiModelProperty(value = "时间")
    private String date;

    @ApiModelProperty(value = "奖品集合")
    private List<UserPrizeVO> prize;
}
