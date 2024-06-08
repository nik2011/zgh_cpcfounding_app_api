package com.yitu.cpcFounding.api.vo.answer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

/**
 * 用户答题表实体
 * @author shenjun
 * @date 2021-02-24 10:15:22
 */
@Data
@ApiModel("用户答题表实体")
public class LsUserAnswerVO {
    /**
    * 答案
    */
    @ApiModelProperty("正确答案")
    private String answer;


    /**
    * 是否正确
    */
    @ApiModelProperty("是否正确")
    private Boolean isCorrect;



}
