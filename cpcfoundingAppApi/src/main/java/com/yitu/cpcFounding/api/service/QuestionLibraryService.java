package com.yitu.cpcFounding.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.answer.LsQuestionLibraryVO;
import com.yitu.cpcFounding.api.vo.answer.LsUserAnswerVO;
import com.yitu.cpcFounding.api.vo.answer.LsUserFinishAnswerVO;

import java.util.List;

/**
 * 题库表服务
 * @author shenjun
 * @date 2021-02-24 10:15:39
 */
public interface QuestionLibraryService {

    /**
    * @Description: 随机获取答题列表
    * @param
    * @author liuzhaowei
    * @date 2021/2/24
    * @return JsonResult<java.util.List<LsQuestionLibraryVO>>
     */
    JsonResult<List<LsQuestionLibraryVO>> getList();


    /**
     * @param
     * @return JsonResult<java.lang.Boolean>
     * @Description: 判断用户是否已经答题
     * @author liuzhaowei
     * @date 2021/2/24
     */
    JsonResult<Boolean> userIsAnswerQuestion();

    /**
     * @param id, answer, answerNumber
     * @return JsonResult<java.lang.Boolean>
     * @Description: 判断用户答题是否正确
     * @author liuzhaowei
     * @date 2021/2/24
     */
    JsonResult<LsUserAnswerVO> userAnswerIsCorrect(long id, String answer, String answerNumber);

    /**
     * @param answerNumber
     * @return JsonResult
     * @Description: 用户完成答题
     * @author liuzhaowei
     * @date 2021/2/24
     */
    JsonResult userFinishAnswer(String answerNumber,long inviteUserId);

    /**
    * @Description:获取题库分页列表
    * @param pageIndex, pageSize
    * @author liuzhaowei
    * @date 2021/6/16
    * @return com.yitu.cpcFounding.api.vo.JsonResult<com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.yitu.cpcFounding.api.vo.answer.LsQuestionLibraryVO>>
     */
    JsonResult<Page<LsQuestionLibraryVO>>getPageList(int pageIndex,int pageSize);
}
