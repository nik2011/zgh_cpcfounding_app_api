package com.yitu.cpcFounding.api.vo.answer;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 题库表实体
 * @author shenjun
 * @date 2021-02-24 10:15:39
 */
@Data
@ApiModel("题库表实体")
public class LsQuestionLibraryVO {
    /**
    * 主键
    */
    @ApiModelProperty("主键")
    private Long id;

    /**
    * 题目
    */
    @ApiModelProperty("题目")
    private String title;


    /**
    * 答题解析
    */
    @ApiModelProperty("答题解析")
    private String answerAnalysis;

    /**
     * 答题编号
     */
    @ApiModelProperty("答题编号")
    private String answerNumber;

    /**
     * 正确答案
     */
    @ApiModelProperty("正确答案")
    private String answer;


    /**
     * 答题选项列表
     */
    @ApiModelProperty("答题选项列表")
    private List<LsQuestionLibraryItemVO> questionLibraryItemVOList;
}
