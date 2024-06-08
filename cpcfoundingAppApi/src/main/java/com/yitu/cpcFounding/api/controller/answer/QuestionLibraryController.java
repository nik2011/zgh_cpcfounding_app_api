package com.yitu.cpcFounding.api.controller.answer;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yitu.cpcFounding.api.auth.LoginRequired;
import com.yitu.cpcFounding.api.service.QuestionLibraryService;
import com.yitu.cpcFounding.api.service.UserAnswerService;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.answer.LsQuestionLibraryVO;
import com.yitu.cpcFounding.api.vo.answer.LsUserAnswerVO;
import com.yitu.cpcFounding.api.vo.answer.LsUserFinishAnswerVO;
import com.yitu.cpcFounding.api.vo.answer.UserAnswerCountVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 题库表控制器
 *
 * @author shenjun
 * @date 2021-02-24 10:15:39
 */
@RestController
@RequestMapping("/api/questionLibrary")
@Api(value = "题库模块", tags = {"题库模块"})
public class QuestionLibraryController {

    @Autowired
    private QuestionLibraryService questionLibraryService;
    @Autowired
    private UserAnswerService userAnswerService;

    /**
     * @param
     * @return JsonResult<LsQuestionLibraryVO>
     * @Description: 随机获取答题列表
     * @author liuzhaowei
     * @date 2021/2/24
     */
    @PostMapping("/getList")
    @ApiOperation(value = "随机获取答题列表")
    @ApiOperationSupport(author = "刘兆伟")
    @LoginRequired
    public JsonResult<List<LsQuestionLibraryVO>> getList() {

        return questionLibraryService.getList();
    }

    /**
     * @param
     * @Description: 判断用户是否已经答题
     * @author liuzhaowei
     * @date 2021/2/24
     */
    @PostMapping("/userIsAnswerQuestion")
    @ApiOperation(value = "判断用户是否已经答题")
    @ApiOperationSupport(author = "刘兆伟")
    @LoginRequired
    public JsonResult<Boolean> userIsAnswerQuestion() {
        return questionLibraryService.userIsAnswerQuestion();
    }

    /**
     * @param
     * @Description: 判断用户答题是否正确
     * @author liuzhaowei
     * @date 2021/2/24
     */
    @PostMapping("/userAnswerIsCorrect")
    @ApiOperation(value = "判断用户答题是否正确")
    @ApiOperationSupport(author = "刘兆伟")
    @LoginRequired
    public JsonResult<LsUserAnswerVO> userAnswerIsCorrect(@ApiParam(value = "题目id") @RequestParam(value = "id") long id,
                                                          @ApiParam(value = "用户答案") @RequestParam(value = "answer") String answer,
                                                          @ApiParam(value = "答题编号") @RequestParam(value = "answerNumber") String answerNumber) {
        return questionLibraryService.userAnswerIsCorrect(id, answer, answerNumber);
    }

    /**
     * @param
     * @Description: 用户完成答题
     * @author liuzhaowei
     * @date 2021/2/24
     */
    @PostMapping("/userFinishAnswer")
    @ApiOperation(value = "用户完成答题")
    @ApiOperationSupport(author = "刘兆伟")
    @LoginRequired
    public JsonResult userFinishAnswer(@ApiParam(value = "答题编号",required = true) @RequestParam(value = "answerNumber") String answerNumber,
                                       @ApiParam(value = "邀请用户id",defaultValue = "0") @RequestParam(value = "inviteUserId",defaultValue = "0") Long inviteUserId) {
        return questionLibraryService.userFinishAnswer(answerNumber,inviteUserId);
    }


    /**
     * @param
     * @Description: 获取答题数量和头像
     * @author liuzhaowei
     * @date 2021/4/10
     */
    @PostMapping("/getAnswerCountAndHead")
    @ApiOperation(value = "获取答题数量和头像")
    @ApiOperationSupport(author = "刘兆伟")
    public JsonResult<UserAnswerCountVO> getAnswerCountAndHead() {
        UserAnswerCountVO userAnswerCountVO = userAnswerService.getAnswerCountAndHead();
        return JsonResult.ok(userAnswerCountVO);
    }

    /**
     * @param pageIndex, pageSize
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.baomidou.mybatisplus.extension.plugins.pagination.Page < com.yitu.cpcFounding.api.vo.answer.LsQuestionLibraryVO>>
     * @Description:获取题库分页列表
     * @author liuzhaowei
     * @date 2021/6/16
     */
    @PostMapping("/getPageList")
    @ApiOperation(value = "获取题库分页列表")
    @ApiOperationSupport(author = "刘兆伟")
    public JsonResult getPageList(@ApiParam(value = "页码") @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                                  @ApiParam(value = "每页数量") @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        return questionLibraryService.getPageList(pageIndex, pageSize);
    }
}
