package com.yitu.cpcFounding.api.service;

import com.yitu.cpcFounding.api.vo.answer.LsQuestionLibraryItemVO;

import java.util.List;

/**
 * 题库选项表服务
 *
 * @author shenjun
 * @date 2021-02-24 10:15:12
 */
public interface QuestionLibraryItemService {

    /**
     * @param questionIds
     * @return java.util.List<LsQuestionLibraryItemVO>
     * @Description: 获取题目选项列表
     * @author liuzhaowei
     * @date 2021/2/24
     */
    List<LsQuestionLibraryItemVO> getQuestionLibraryItemList(List<Long> questionIds);

}
