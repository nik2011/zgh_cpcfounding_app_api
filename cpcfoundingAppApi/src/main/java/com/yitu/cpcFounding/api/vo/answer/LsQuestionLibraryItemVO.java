package com.yitu.cpcFounding.api.vo.answer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

/**
 * 题库选项表实体
 * @author shenjun
 * @date 2021-02-24 10:15:12
 */
@Data
@ApiModel("题库选项表实体")
public class LsQuestionLibraryItemVO {
    /**
    * 主键
    */
    @ApiModelProperty("主键")
    private Long id;

    /**
    * 题目id
    */
    @ApiModelProperty("题目id")
    private Long questionId;

    /**
    * 名称
    */
    @ApiModelProperty("名称")
    private String name;

    /**
    * 编号 a,b,c,d
    */
    @ApiModelProperty("编号 a,b,c,d")
    private String number;


}
