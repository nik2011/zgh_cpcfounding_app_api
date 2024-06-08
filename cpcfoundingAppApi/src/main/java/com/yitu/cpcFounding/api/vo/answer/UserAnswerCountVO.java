package com.yitu.cpcFounding.api.vo.answer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 用户答题表实体
 * @author 刘兆伟
 * @date 2021-02-24 10:15:22
 */
@Data
@ApiModel("用户答题数量vo")
public class UserAnswerCountVO {
    /**
    * 数量
    */
    @ApiModelProperty("数量")
    private long count;


    /**
    * 头像列表
    */
    @ApiModelProperty("头像列表")
    private List<String> headList;



}
