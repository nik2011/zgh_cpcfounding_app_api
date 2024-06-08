package com.yitu.cpcFounding.api.service.impl;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.sun.xml.bind.v2.TODO;
import com.yitu.cpcFounding.api.constant.Configs;
import com.yitu.cpcFounding.api.domain.*;
import com.yitu.cpcFounding.api.enums.*;
import com.yitu.cpcFounding.api.exception.ConsciousException;
import com.yitu.cpcFounding.api.mapper.UserScoreMapper;
import com.yitu.cpcFounding.api.rabbitmq.Producer.RabbitmqProducer;
import com.yitu.cpcFounding.api.redis.RedisUtil;
import com.yitu.cpcFounding.api.service.*;
import com.yitu.cpcFounding.api.utils.HttpUtil;
import com.yitu.cpcFounding.api.auth.LoginUserUtil;
import com.yitu.cpcFounding.api.mapper.QuestionLibraryMapper;
import com.yitu.cpcFounding.api.mapper.UserAnswerMapper;
import com.yitu.cpcFounding.api.utils.IpUtil;
import com.yitu.cpcFounding.api.utils.UuidUtils;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.UserVO;
import com.yitu.cpcFounding.api.vo.answer.LsQuestionLibraryItemVO;
import com.yitu.cpcFounding.api.vo.answer.LsQuestionLibraryVO;
import com.yitu.cpcFounding.api.vo.answer.LsUserAnswerVO;
import com.yitu.cpcFounding.api.vo.answer.LsUserFinishAnswerVO;
import com.yitu.cpcFounding.api.vo.prizeBook.ActiveTimeVo;
import com.yitu.cpcFounding.api.vo.redis.DynamicUserVO;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 题库表服务实现
 *
 * @author shenjun
 * @date 2021-02-24 10:15:39
 */
@Service
public class QuestionLibraryServiceImpl implements QuestionLibraryService {

    @Autowired
    private QuestionLibraryMapper questionLibraryMapper;
    @Autowired
    private QuestionLibraryItemService lsQuestionLibraryItemService;
    @Autowired
    private LoginUserUtil loginUserUtil;
    @Autowired
    private CommonService commonService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserAnswerMapper userAnswerMapper;
    @Autowired
    private PrizeBookService prizeBookService;
    @Autowired
    private UserScoreMapper userScoreMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RabbitmqProducer rabbitmqProducer;
    @Resource
    private RedisUtil redisUtil;

    // 答题锁对象
    private Lock answerLock = new ReentrantLock();

    /**
     * @return JsonResult<java.util.List < LsQuestionLibraryVO>>
     * @Description: 随机获取答题列表
     * @author liuzhaowei
     * @date 2021/2/24
     */
    @Override
    public JsonResult<List<LsQuestionLibraryVO>> getList() {
        // UserVO userVO = loginUserUtil.getLoginUser();
//        if (userVO.getMemberState() == MemberState.Common.getCode()) {
//            return JsonResult.fail("非会员不能参加答题，请先认证");
//        }
        int count = Configs.ANSWER_COUNT;
        List<LsQuestionLibraryVO> resultList = questionLibraryMapper.getRandomList(count);
        if (resultList.size() != count) {
            return JsonResult.fail("题库数量不足");
        }
        List<Long> questionIds = resultList.stream().map(k -> k.getId()).collect(Collectors.toList());
        List<LsQuestionLibraryItemVO> libraryItemVOList = lsQuestionLibraryItemService.getQuestionLibraryItemList(questionIds);
        String answerNumber = UUID.randomUUID().toString().replace("-", "");
        Map<String, Object> map = new HashMap<>();
        for (LsQuestionLibraryVO item : resultList) {
            List<LsQuestionLibraryItemVO> selectList = libraryItemVOList.stream().filter(k -> k.getQuestionId().equals(item.getId())).collect(Collectors.toList());

            item.setQuestionLibraryItemVOList(selectList);
            item.setAnswerNumber(answerNumber);

            long tempId = UuidUtils.getId();
            map.put(String.valueOf(tempId), item.getId() + "-" + item.getAnswer());
            item.setId(tempId);
            item.setAnswer("");
        }
        String key = Configs.USER_ANSWER + answerNumber;
        redisUtil.hmset(key, map, Configs.USER_ANSWER_TIMEOUT);
        return JsonResult.ok(resultList);
    }


    /**
     * @return JsonResult<java.lang.Boolean>
     * @Description: 判断用户是否已经答题
     * @author liuzhaowei
     * @date 2021/2/24
     */
    @Override
    public JsonResult<Boolean> userIsAnswerQuestion() {
        UserVO userVO = loginUserUtil.getLoginUser();
        DynamicUserVO dynamicUserVO = commonService.getUserDynamicInfo(userVO.getId(), userVO.getSessionId());
        if (dynamicUserVO != null && dynamicUserVO.getWhetherAnswer()) {
            return JsonResult.ok(true);
        }
        return JsonResult.ok(false);
    }

    /**
     * @param id
     * @param answer
     * @param answerNumber
     * @return JsonResult<java.lang.Boolean>
     * @Description: 判断用户答题是否正确
     * @author liuzhaowei
     * @date 2021/2/24
     */
    @Override
    public JsonResult<LsUserAnswerVO> userAnswerIsCorrect(long id, String answer, String answerNumber) {
        // UserVO userVO = loginUserUtil.getLoginUser();
//        if (userVO.getMemberState() == MemberState.Common.getCode()) {
//            return JsonResult.fail("非会员不能参加答题，请先认证");
//        }
        String loginUserName = loginUserUtil.getUserName();
        long loginUserId = loginUserUtil.getUserId();
        JsonResult activeTimeJson = prizeBookService.getActiveTime(ActiveEntranceEnum.ANSWER.getValue());
        if (activeTimeJson.getStatus() == ResponseCode.INTERNAL_SERVER_ERROR.getCode()) {
            return JsonResult.fail(activeTimeJson.getMsg());
        }
        ActiveTimeVo activeTimeVo = (ActiveTimeVo) activeTimeJson.getData();
        if (activeTimeVo.getStatus() == 0) {
            return JsonResult.fail("活动未开始");
        } else if (activeTimeVo.getStatus() == 2) {
            return JsonResult.fail("活动已结束");
        }
        if (StringUtils.isBlank(answer)) {
            return JsonResult.fail("用户答案不能为空");
        }
        if (StringUtils.isBlank(answerNumber)) {
            return JsonResult.fail("答题编号不能为空");
        }
//        QuestionLibrary questionLibrary = questionLibraryMapper.selectById(id);
//        if (questionLibrary == null || questionLibrary.getDeleted() == 1) {
//            return JsonResult.fail("题目不存在");
//        }
//        int questionCount = userAnswerMapper.selectCount(new LambdaQueryWrapper<UserAnswer>()
//                .eq(UserAnswer::getDeleted, 0)
//                .eq(UserAnswer::getQuestionId, id)
//                .eq(UserAnswer::getAnswerNumber, answerNumber)
//        );
        if (!redisUtil.hasKey(Configs.USER_ANSWER + answerNumber)) {
            return JsonResult.fail("答题编号不存在");
        }
        //判断该题目是否答过
        Object answerObj = redisUtil.hget(Configs.USER_ANSWER + answerNumber, String.valueOf(id));
        if (ObjectUtils.isEmpty(answerObj)) {
            return JsonResult.fail("您该题目已经答过");
        }
        boolean isCorrect = false;
        long querstionId = Long.valueOf(answerObj.toString().split("-")[0]);
        String answerRigth = answerObj.toString().split("-")[1];
        if (StringUtils.equalsIgnoreCase(answer, answerRigth)) {
            isCorrect = true;
            redisUtil.incr(Configs.USER_ANSWER_RIGHT_COUNT + answerNumber, 1);
            redisUtil.expire(Configs.USER_ANSWER_RIGHT_COUNT + answerNumber, Configs.USER_ANSWER_TIMEOUT);
        }
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuestionId(querstionId);
        userAnswer.setAnswer(answer);
        userAnswer.setAnswerNumber(answerNumber);
        userAnswer.setIsCorrect(isCorrect ? 1 : 0);
        userAnswer.setUserId(loginUserId);
        userAnswer.setUserName(loginUserName);
        userAnswer.setAddDate(new Date());
        userAnswer.setAddUser(loginUserName);
        userAnswer.setModifyDate(new Date());
        userAnswer.setModifyUser(loginUserName);
        userAnswer.setIp(HttpUtil.getIPAddress(request));
        userAnswer.setDeleted(0);
        Gson gson = new Gson();
        redisUtil.lSet(Configs.USER_ANSWER_ENTITY + answerNumber, gson.toJson(userAnswer), Configs.USER_ANSWER_TIMEOUT);
        //int result = userAnswerMapper.insert(userAnswer);

//        if (result == 0) {
//            throw new ConsciousException(ResponseCode.INTERNAL_SERVER_ERROR, "添加记录失败");
//        }

        //答题完成把答案清空
        redisUtil.hdel(Configs.USER_ANSWER + answerNumber, String.valueOf(id));
        LsUserAnswerVO userAnswerVO = new LsUserAnswerVO();
        userAnswerVO.setAnswer(answerRigth);
        userAnswerVO.setIsCorrect(isCorrect);

        return JsonResult.ok(userAnswerVO);
    }

    /**
     * @param answerNumber
     * @return JsonResult
     * @Description: 用户完成答题
     * @author liuzhaowei
     * @date 2021/2/24
     */
    @Override
    public JsonResult userFinishAnswer(String answerNumber, long inviteUserId) {
        JsonResult activeTimeJson = prizeBookService.getActiveTime(ActiveEntranceEnum.ANSWER.getValue());
        if (activeTimeJson.getStatus() == ResponseCode.INTERNAL_SERVER_ERROR.getCode()) {
            return JsonResult.fail(activeTimeJson.getMsg());
        }
        ActiveTimeVo activeTimeVo = (ActiveTimeVo) activeTimeJson.getData();
        if (activeTimeVo.getStatus() == 0) {
            return JsonResult.fail("活动未开始");
        } else if (activeTimeVo.getStatus() == 2) {
            return JsonResult.fail("活动已结束");
        }
        UserVO userVO = loginUserUtil.getLoginUser();

        if (StringUtils.isBlank(answerNumber)) {
            return JsonResult.fail("答题编号不能为空");
        }
//        if (dynamicUserVO.getWhetherAnswer()) {
//            return JsonResult.fail("您今天已经完成答题了");
//        }
//        long addScoreCount = userScoreMapper.selectCount(new LambdaQueryWrapper<UserScore>()
//                .eq(UserScore::getDeleted, DeletedEnum.NOT_DELETE.getValue())
//                .eq(UserScore::getSourceType, SourceTypeEnum.DAY_ANSWER.getValue())
//                .eq(UserScore::getTargetId, answerNumber)
//        );

        if (!redisUtil.hasKey(Configs.USER_ANSWER_ENTITY + answerNumber)) {
            return JsonResult.fail("答题数据不存在");
        }
        //完成答题
//        List<UserAnswer> userAnswerList = userAnswerMapper.selectList(new LambdaQueryWrapper<UserAnswer>()
//                .eq(UserAnswer::getDeleted, DeletedEnum.NOT_DELETE.getValue())
//                .eq(UserAnswer::getUserId, userVO.getId())
//                .eq(UserAnswer::getAnswerNumber, answerNumber)
//        );

        List<Object> objectList = redisUtil.lGet(Configs.USER_ANSWER_ENTITY + answerNumber, 0, 10);
        if (objectList.size() != Configs.ANSWER_COUNT) {
            return JsonResult.fail("您答题数量不足");
        }
        boolean isAddScroe = false;
        answerLock.lock();
        try {
            DynamicUserVO dynamicUserVO = commonService.getUserDynamicInfo(userVO.getId(), userVO.getSessionId());
            if (dynamicUserVO == null) {
                return JsonResult.fail("用户不存在");
            }
            //答题次数
            int answerCount = dynamicUserVO.getAnswerCount();
            if (answerCount < Configs.FINISH_ANSWER_COUNT) {
                isAddScroe = true;
            }
            //会员答题加1
            if (userVO.getMemberState().equals(MemberState.Member.getCode())) {
                dynamicUserVO.setAnswerCount(dynamicUserVO.getAnswerCount() + 1);
                boolean result = commonService.setUserDynamicInfo(userVO.getId(), dynamicUserVO);
                if (!result) {
                    return JsonResult.fail("系统繁忙，请重试！");
                }
            }
        } finally {
            answerLock.unlock();
        }
        LsUserFinishAnswerVO lsUserFinishAnswerVO = new LsUserFinishAnswerVO();
        //正确数量
        long correctCount = 0;
        if (redisUtil.hasKey(Configs.USER_ANSWER_RIGHT_COUNT + answerNumber)) {
            correctCount = (int) redisUtil.get(Configs.USER_ANSWER_RIGHT_COUNT + answerNumber);
        }
        lsUserFinishAnswerVO.setAnswerCount(correctCount);

        boolean scoreActivity = false;//积分活动是否开启
        JsonResult scoreActivityJson = prizeBookService.getActiveTime(ActiveEntranceEnum.USER_SCORE.getValue());
        if (scoreActivityJson.getStatus() == ResponseCode.INTERNAL_SERVER_ERROR.getCode()) {
            return JsonResult.fail(scoreActivityJson.getMsg());
        }
        ActiveTimeVo activeTimeVoScore = (ActiveTimeVo) scoreActivityJson.getData();
        if (activeTimeVoScore.getStatus() == 1) {
            scoreActivity = true;
        }
        if (correctCount > 0 && userVO.getMemberState().equals(MemberState.Member.getCode())
                && isAddScroe && scoreActivity) {
            long addScore = correctCount * SourceTypeNumEnum.DAY_ANSWER.getName();
            lsUserFinishAnswerVO.setAddScore(addScore);
            Integer sum = userScoreMapper.selectSumOfDayByScoreTypeAndUserId(SourceTypeEnum.DAY_ANSWER.getValue(), userVO.getId());
            int maxScore = SourceTypeNumEnum.DAY_ANSWER_COUNT.getName();
            if (sum + addScore >= maxScore) {
                lsUserFinishAnswerVO.setOverScroe(true);
                addScore = maxScore - sum;
                //return JsonResult.fail("答题可获取积分已达上限");
            }
            if (addScore > 0) {
                User user = userService.findUserById(userVO.getId());
                UserScore userScore = new UserScore();
                userScore.setAddUser(userVO.getWxUserName());
                userScore.setWxUserName(userVO.getWxUserName());
                userScore.setAddDate(new Date());
                userScore.setDeleted(DeletedEnum.NOT_DELETE.getValue());
                userScore.setIp(IpUtil.getIpAdrress(request));
                userScore.setPhone(user.getPhone());
                userScore.setTeamId(Long.parseLong(user.getTeamId() + ""));
                userScore.setUserId(userVO.getId());
                userScore.setScore(Integer.valueOf(String.valueOf(addScore)));
                userScore.setSourceType(SourceTypeEnum.DAY_ANSWER.getValue());
                userScore.setTargetId(answerNumber);
                // 加入消息队列，添加积分明细
                Gson gson = new Gson();
                String userScoreData = gson.toJson(userScore);
                rabbitmqProducer.sendMessage(QueueEnum.USER_SCORE_QUEUE, userScoreData);
            }
        }
        // 执行消息队列，批量插入数据
        rabbitmqProducer.sendMessage(QueueEnum.USER_ANSWER_QUEUE, answerNumber);

        //删除答题缓存
        redisUtil.del(Configs.USER_ANSWER + answerNumber);

        if (inviteUserId > 0 && !userVO.getId().equals(inviteUserId) && scoreActivity) {
            User inviteUser = userService.findUserById(inviteUserId);
            if (inviteUser != null && inviteUser.getMemberState() == MemberState.Member.getCode()) {
                //邀请答题用户有效，判断该用户是否已邀请过当前用户
                int count = userScoreMapper.checkUserShareAnswer(userVO.getId(), inviteUserId);
                if (count == 0) {
                    //当前用户未邀请过
                    // 邀请好友完成答题 邀请人 一次积分20分 上限100分
                    Integer sum = userScoreMapper.selectSumOfDayByScoreTypeAndUserId(SourceTypeEnum.INVITE_FRIEND.getValue(), inviteUserId);
                    if (sum == null || sum <= SourceTypeNumEnum.INVITE_FRIEND_COUNT.getName()) {
                        UserScore userScore = new UserScore();
                        userScore.setAddUser(inviteUser.getWxUserName());
                        userScore.setAddDate(new Date());
                        userScore.setDeleted(DeletedEnum.NOT_DELETE.getValue());
                        userScore.setIp(IpUtil.getIpAdrress(request));
                        userScore.setPhone(inviteUser.getPhone());
                        userScore.setTeamId(Long.parseLong(inviteUser.getTeamId() + ""));
                        userScore.setUserId(inviteUser.getId());
                        userScore.setTargetId(userVO.getId() + "");
                        userScore.setWxUserName(inviteUser.getWxUserName());
                        userScore.setScore(SourceTypeNumEnum.INVITE_FRIEND.getName());
                        userScore.setSourceType(SourceTypeEnum.INVITE_FRIEND.getValue());
                        // 加入消息队列，添加积分明细
                        Gson gson = new Gson();
                        String userScoreData = gson.toJson(userScore);
                        rabbitmqProducer.sendMessage(QueueEnum.USER_SCORE_QUEUE, userScoreData);
                    }
                }
            }
        }

        return JsonResult.ok(lsUserFinishAnswerVO);
    }

    /**
     * @param pageIndex
     * @param pageSize
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.baomidou.mybatisplus.extension.plugins.pagination.Page < com.yitu.cpcFounding.api.vo.answer.LsQuestionLibraryVO>>
     * @Description:获取题库分页列表
     * @author liuzhaowei
     * @date 2021/6/16
     */
    @Override
    public JsonResult<Page<LsQuestionLibraryVO>> getPageList(int pageIndex, int pageSize) {
        pageSize = pageSize > com.yitu.cpcFounding.api.constant.Page.PAGE_SIZE_50 ? com.yitu.cpcFounding.api.constant.Page.PAGE_SIZE_50 : pageSize;
        Page<QuestionLibrary> page = new Page<>(pageIndex, pageSize);

        IPage<QuestionLibrary> questionLibraryList = questionLibraryMapper.selectPage(page, new LambdaQueryWrapper<QuestionLibrary>()
                .eq(QuestionLibrary::getDeleted, DeletedEnum.NOT_DELETE.getValue())
                .orderByAsc(QuestionLibrary::getId)
        );

        List<Long> questionIds = questionLibraryList.getRecords().stream().map(k -> k.getId()).collect(Collectors.toList());
        List<LsQuestionLibraryItemVO> libraryItemVOList = lsQuestionLibraryItemService.getQuestionLibraryItemList(questionIds);

        List<LsQuestionLibraryVO> resultList = new ArrayList<>();
        for (QuestionLibrary item : questionLibraryList.getRecords()) {
            LsQuestionLibraryVO lsQuestionLibraryVO = new LsQuestionLibraryVO();
            BeanUtils.copyProperties(item, lsQuestionLibraryVO);
            List<LsQuestionLibraryItemVO> selectList = libraryItemVOList.stream().filter(k -> k.getQuestionId().equals(item.getId())).collect(Collectors.toList());
            lsQuestionLibraryVO.setQuestionLibraryItemVOList(selectList);
            lsQuestionLibraryVO.setId(null);
            resultList.add(lsQuestionLibraryVO);
        }
        Page<LsQuestionLibraryVO> resultPage = new Page<>();
        BeanUtils.copyProperties(page, resultPage);
        resultPage.setRecords(resultList);
        return JsonResult.ok(resultPage);
    }
}
