package com.yitu.cpcFounding.api.vo.userScore;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 个人列表以及当前用户所属信息
 * @author wangping
 * @version 1.0
 * @date 2021/04/09
 */
@Data
@ApiModel("个人列表以及当前用户所属信息")
public class UserListVO {

    @ApiModelProperty("个人列表")
    private IPage<UserRankingListVO> topList;

    @ApiModelProperty("当前登入用户信息")
    private UserRankingListVO self;

    @ApiModelProperty("当前用户是否实名 true已实名，false未实名")
    private  Boolean userState;

}
