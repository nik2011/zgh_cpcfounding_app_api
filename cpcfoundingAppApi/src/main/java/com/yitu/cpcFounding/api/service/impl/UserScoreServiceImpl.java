package com.yitu.cpcFounding.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yitu.cpcFounding.api.auth.LoginUserUtil;
import com.yitu.cpcFounding.api.constant.Configs;
import com.yitu.cpcFounding.api.domain.*;
import com.yitu.cpcFounding.api.enums.*;
import com.yitu.cpcFounding.api.mapper.UserAnswerMapper;
import com.yitu.cpcFounding.api.mapper.UserScoreMapper;
import com.yitu.cpcFounding.api.po.DatePo;
import com.yitu.cpcFounding.api.rabbitmq.Producer.RabbitmqProducer;
import com.yitu.cpcFounding.api.redis.RedisUtil;
import com.yitu.cpcFounding.api.service.*;
import com.yitu.cpcFounding.api.utils.*;
import com.yitu.cpcFounding.api.vo.*;
import com.yitu.cpcFounding.api.vo.prizeBook.ActiveTimeVo;
import com.yitu.cpcFounding.api.vo.redis.DynamicUserVO;
import com.yitu.cpcFounding.api.vo.userScore.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户积分表服务实现
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/7 14:55
 */

@Service
@Slf4j
public class UserScoreServiceImpl implements UserScoreService {
    @Autowired
    private LoginUserUtil loginUserUtil;
    @Autowired
    private UserScoreMapper userScoreMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WeekRankingUtil weekRankingUtil;
    @Autowired
    private RabbitmqProducer rabbitmqProducer;
    @Resource
    private HttpServletRequest request;
    @Autowired
    private ConfigService configService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private PrizeBookService prizeBookService;
    @Autowired
    private CommonService commonService;


    /**
     * 获取个人榜
     *
     * @param type      排行榜类型
     * @param pageSize  显示条数
     * @param pageIndex 页码
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.UserScoreVO>
     * @author qixinyi
     * @date 2021/6/8 15:50
     */
    @Override
    public JsonResult<UserScoreVO> findScoreSumByType(Integer type, int pageIndex, int pageSize) {
        UserVO loginUser = loginUserUtil.getLoginUser();

        // 校验
        if (!EnumUtil.isExist(RankingEnum.class, type)) {
            return JsonResult.fail("排行榜类型状态码数据非法");
        }

        JsonResult activeTimeJson = prizeBookService.getActiveTime(ActiveEntranceEnum.USER_SCORE.getValue());
        if (activeTimeJson.getStatus() == ResponseCode.INTERNAL_SERVER_ERROR.getCode()) {
            return JsonResult.fail(activeTimeJson.getMsg());
        }
        ActiveTimeVo activeTimeVo = (ActiveTimeVo) activeTimeJson.getData();
        Integer status = activeTimeVo.getStatus();

        // 封装
        UserScoreVO userScoreVO = new UserScoreVO();
        if (loginUser != null) {
            Long id = loginUser.getId();

            // 封装个人信息
            String key = "";
            if(type==RankingEnum.TOTAL_RANKING.getValue()){
                key=Configs.REDIS_ACTIVE_USER_SORT;
            }else if(type==RankingEnum.WEEK_RANKING.getValue()){
                key=Configs.REDIS_ACTIVE_USER_WEEK_SORT;
            }

            Gson gson = new Gson();
            // 判断键值是否存在
            boolean userScoreKey = redisUtil.hHasKey(key, id.toString());
            if (userScoreKey) {
                String userScoreJson = redisUtil.hget(key, id.toString()).toString();
                if (!org.apache.commons.lang3.StringUtils.isEmpty(userScoreJson)) {
                    UserRankingListVO userScore = gson.fromJson(userScoreJson, UserRankingListVO.class);
                    // 获取用户总榜排名
                    userScoreVO.setSelf(userScore);
                }
            }

            if (RankingEnum.TOTAL_RANKING.getValue() == type) {
                //saveUserScoreVO(pageIndex, pageSize, id, userScoreVO, Configs.REDIS_ACTIVE_USER_RANK, RankingTypeEnum.USER_RANKING.getValue());
                // 查询
                IPage<UserRankingListVO> pages = getUserTotalRankingPage(pageIndex, pageSize,RankingEnum.TOTAL_RANKING);
                userScoreVO.setTopList(pages);

                //添加提示语
                userScoreVO.setUserScoreLabelVO(getRankingLabel(RankingLabelEnum.RANKING_LABEL_2.getValue()));
            } else if (RankingEnum.WEEK_RANKING.getValue() == type) {
                //saveUserScoreVO(pageIndex, pageSize, id, userScoreVO, Configs.REDIS_ACTIVE_USER_WEEK_RANK, RankingTypeEnum.USER_WEEK_RANKING.getValue());
                // 查询
                IPage<UserRankingListVO> pages = getUserTotalRankingPage(pageIndex, pageSize,RankingEnum.WEEK_RANKING);
                userScoreVO.setTopList(pages);

                DatePo weekDate = null;
                if (status == ActiveStatusEnum.ING.getValue()) {
                    weekDate = weekRankingUtil.getWeekDate();
                } else {
                    weekDate = weekRankingUtil.getLastWeek();
                }
                if (weekDate != null) {
                    String begin = weekDate.getBeginDate().substring(5, 10).replace("-", ".").replaceFirst("0", "");
                    String end = weekDate.getEndDate().substring(5, 10).replace("-", ".").replaceFirst("0", "");
                    userScoreVO.setTime(begin + "-" + end);
                }

                //添加提示语
                userScoreVO.setUserScoreLabelVO(getRankingLabel(RankingLabelEnum.RANKING_LABEL_1.getValue()));
            }
        }
        return JsonResult.ok(userScoreVO);
    }

    /**
     * 获取用户总榜/周榜分页数据
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    private IPage<UserRankingListVO> getUserTotalRankingPage(int pageIndex, int pageSize,RankingEnum rankingEnum) {
        String key = "";
        switch (rankingEnum){
            case TOTAL_RANKING:
                key = Configs.RANKING_PAGE_USER_TOTAL;
                break;
            case WEEK_RANKING:
                key = Configs.RANKING_PAGE_USER_WEEK;
        }
        //大于缓存条数时，直接从数据库取
        if (pageIndex > Configs.pageIndexRedis) {
            return getUserRankingListVOIpage(pageIndex, pageSize,rankingEnum);
        }
        Gson gson = new Gson();
        boolean hasKey = redisUtil.hasKey(key);
        if (hasKey) {
            //直接从缓存中取
            Object obj = redisUtil.hget(key, pageIndex + "");
            Type type = new TypeToken<Page<UserRankingListVO>>() {
            }.getType();
            IPage<UserRankingListVO> pages = gson.fromJson(obj.toString(), type);
            return pages;
        }
        return null;
    }

    /**
     * 获取个人总榜/周榜排名分页
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    private IPage<UserRankingListVO> getUserRankingListVOIpage(int pageIndex, int pageSize,RankingEnum rankingEnum) {
        Page<UserRankingListVO> page = new Page<>(pageIndex, pageSize);
        IPage<UserRankingListVO> pages = null;
        switch (rankingEnum){
            case TOTAL_RANKING:
                pages = userScoreMapper.selectUserScoreRankings(page);
                break;
            case WEEK_RANKING:
                pages = userScoreMapper.selectUserScoreWeekRankings(page);
                break;
        }
        //处理排名
        List<UserRankingListVO> records = pages.getRecords();
        if (records != null && records.size() > 0) {
            for (int j = 0; j < records.size(); j++) {
                records.get(j).setUserSort(pageSize * (pageIndex - 1) + 1 + j);
            }
        }
        pages.setRecords(records);
        return pages;
    }

    /**
     * 封装排行榜数据
     *
     * @param pageIndex   页码
     * @param pageSize    页大小
     * @param userId      用户id
     * @param userScoreVO 个人排行榜列表以及当前用户所属信息实体对象
     * @param key         key值
     * @param type        类型
     * @return void
     * @author qixinyi
     * @date 2021/7/2 15:51
     */
    private void saveUserScoreVO(int pageIndex, int pageSize, Long userId, UserScoreVO userScoreVO, String key, Integer type) {
        // 封装排行榜
        Map map = redisUtil.hmget(key);

        if (map != null && map.size() > 0) {
            List<UserRankingListVO> result = (List<UserRankingListVO>) map.values().stream().sorted().map(o -> {
                UserRankingListVO realUserScoreVO = JSON.parseObject(JSON.parse(o.toString()).toString(), UserRankingListVO.class);
                return realUserScoreVO;
            }).collect(Collectors.toList());

            //数据排序
            List<UserRankingListVO> results = result.stream().sorted(Comparator.comparing(UserRankingListVO::getUserSort)).collect(Collectors.toList());

            IPage page = PageUtil.getPages(pageIndex, pageSize, results);

            userScoreVO.setTopList(page);

            // 封装个人信息
            String userJson = (String) map.get(userId.toString());
            if (userJson != null) {
                UserRankingListVO realLoginUserScoreVO = JSON.parseObject(JSON.parse(userJson).toString(), UserRankingListVO.class);
                if (realLoginUserScoreVO != null && realLoginUserScoreVO.getId().equals(userId)) {
                    userScoreVO.setSelf(realLoginUserScoreVO);
                }
            }
        }
    }

    /**
     * 获取积分明细
     *
     * @param pageSize  显示条数
     * @param pageIndex 页码
     * @return com.yitu.cpcFounding.api.vo.JsonResult<java.util.List < com.yitu.cpcFounding.api.vo.UserScoreDetailVO>>
     * @author qixinyi
     * @date 2021/6/7 15:55
     */
    @Override
    public JsonResult<List<UserScoreDetailVO>> findUserScoreDetails(int pageIndex, int pageSize) {
        // 获取用户时间
        long userId = loginUserUtil.getUserId();

        // 获取所有添加时间
        List<UserScoreDetailVO> list = null;
        if (userId > 0) {
            List<Date> dates = userScoreMapper.findAddDateByUserId(userId);

            list = new ArrayList<>();
            if (dates != null && dates.size() > 0) {
                for (Date date : dates) {
                    // 封装时间
                    UserScoreDetailVO userScoreDetailVO = new UserScoreDetailVO();
                    userScoreDetailVO.setAddDate(date);

                    // 查询积分详情列表
                    Page<UserScoreDetailVO> page = new Page<>(pageIndex, pageSize);
                    String realDate = DateTimeUtil.format(date, "yyyy-MM-dd");
                    IPage<UserScoreDetailsVO> pages = userScoreMapper.selectUserScoreDetails(page, userId, realDate);

                    List<UserScoreDetailsVO> records = pages.getRecords();
                    if (records != null && records.size() > 0) {
                        // 封装积分来源类型名
                        for (UserScoreDetailsVO record : records) {
                            Integer sourceType = record.getSourceType();
                            if (sourceType == SourceTypeEnum.SIGIN_IN.getValue()) {
                                record.setSourceTypeName(SourceTypeEnum.SIGIN_IN.getName());
                            } else if (sourceType == SourceTypeEnum.READ_ARTICLE.getValue()) {
                                record.setSourceTypeName(SourceTypeEnum.READ_ARTICLE.getName());
                            } else if (sourceType == SourceTypeEnum.DAY_ANSWER.getValue()) {
                                record.setSourceTypeName(SourceTypeEnum.DAY_ANSWER.getName());
                            } else if (sourceType == SourceTypeEnum.DAY_FIRST_SHARE.getValue()) {
                                record.setSourceTypeName(SourceTypeEnum.DAY_FIRST_SHARE.getName());
                            } else if (sourceType == SourceTypeEnum.INVITE_FRIEND.getValue()) {
                                record.setSourceTypeName(SourceTypeEnum.INVITE_FRIEND.getName());
                            } else if (sourceType == SourceTypeEnum.REWARD.getValue()) {
                                record.setSourceTypeName(SourceTypeEnum.REWARD.getName());
                            } else if (sourceType == SourceTypeEnum.JOIN_TEAM.getValue()) {
                                record.setSourceTypeName(SourceTypeEnum.JOIN_TEAM.getName());
                            } else if (sourceType == SourceTypeEnum.ROTARY_DRAW.getValue()) {
                                record.setSourceTypeName(SourceTypeEnum.ROTARY_DRAW.getName());
                            } else if (sourceType == SourceTypeEnum.COMPANY_REWARD.getValue()) {
                                record.setSourceTypeName(SourceTypeEnum.COMPANY_REWARD.getName());
                            } else if (sourceType == SourceTypeEnum.STREET_REWARD.getValue()) {
                                record.setSourceTypeName(SourceTypeEnum.STREET_REWARD.getName());
                            } else if (sourceType == SourceTypeEnum.AREA_REWARD.getValue()) {
                                record.setSourceTypeName(SourceTypeEnum.AREA_REWARD.getName());
                            }
                        }

                        pages.setRecords(records);

                        // 封装积分详情列表
                        userScoreDetailVO.setUserScoreDetailsVO(pages);
                    }

                    // 封装总积分数
                    Integer sum = userScoreMapper.selectSumOfDayByAddDate(userId, realDate);
                    userScoreDetailVO.setSum(sum);

                    // 封装数据
                    list.add(userScoreDetailVO);
                }
            }
            return JsonResult.ok(list);
        }

        return JsonResult.fail("请先登录");
    }

    /**
     * 获取答题总榜分页数据
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    private IPage<UserRankingListVO> getAnswerTotalRankingPage(int pageIndex, int pageSize) {
        String key = Configs.RANKING_PAGE_ANSWER_TOTAL;
        //大于缓存条数时，直接从数据库取
        if (pageIndex > Configs.pageIndexRedis) {
            return getAnswerTotalRankingListVOIpage(pageIndex, pageSize);
        }
        Gson gson = new Gson();
        boolean hasKey = redisUtil.hasKey(key);
        if (hasKey) {
            //直接从缓存中取
            Object obj = redisUtil.hget(key, pageIndex + "");
            Type type = new TypeToken<Page<UserRankingListVO>>() {
            }.getType();
            IPage<UserRankingListVO> pages = gson.fromJson(obj.toString(), type);
            return pages;
        }
        return null;
    }

    /**
     * 获取个人总榜/周榜排名分页
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    private IPage<UserRankingListVO> getAnswerTotalRankingListVOIpage(int pageIndex, int pageSize) {
        Page<UserRankingListVO> page = new Page<>(pageIndex, pageSize);
        IPage<UserRankingListVO> pages=userScoreMapper.selectAnswerScoreRankings(page);
        // 封装排名
        List<UserRankingListVO> records = pages.getRecords();
        if (records!=null && records.size()>0){
            for (int i = 0; i < records.size(); i++) {
                records.get(i).setUserSort(pageSize*(pageIndex-1)+1+i);
            }
        }
        // 封装列表
        pages.setRecords(records);
        return pages;
    }

    /**
     * 获取答题榜
     *
     * @param pageSize  显示条数
     * @param pageIndex 页码
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.UserScoreVO>
     * @author qixinyi
     * @date 2021/6/9 10:34
     */
    @Override
    public JsonResult<UserScoreVO> findAnswerScoreSum(int pageIndex, int pageSize) {
        UserVO loginUser = loginUserUtil.getLoginUser();

        // 封装
        UserScoreVO userScoreVO = new UserScoreVO();
        if (loginUser != null) {
            Long id = loginUser.getId();

            // 封装个人信息
            Gson gson = new Gson();
            // 判断键值是否存在
            boolean userScoreKey = redisUtil.hHasKey(Configs.REDIS_ACTIVE_ANSWER_SORT, id.toString());
            if (userScoreKey) {
                String userScoreJson = redisUtil.hget(Configs.REDIS_ACTIVE_ANSWER_SORT, id.toString()).toString();
                if (!org.apache.commons.lang3.StringUtils.isEmpty(userScoreJson)) {
                    UserRankingListVO userScore = gson.fromJson(userScoreJson, UserRankingListVO.class);
                    // 获取用户总榜排名
                    userScoreVO.setSelf(userScore);
                }
            }

//            saveUserScoreVO(pageIndex, pageSize, loginUserUtil.getUserId(), userScoreVO, Configs.REDIS_ACTIVE_ANSWER_RANK, RankingTypeEnum.ANSWER_RANKING.getValue());

            // 查询
            IPage<UserRankingListVO> pages=getAnswerTotalRankingPage(pageIndex,pageSize);
            userScoreVO.setTopList(pages);
        }

        //添加提示语
        userScoreVO.setUserScoreLabelVO(getRankingLabel(RankingLabelEnum.RANKING_LABEL_9.getValue()));

        return JsonResult.ok(userScoreVO);
    }

    /**
     * 保存用户总榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/8 10:09
     */
    @Override
    public void saveUserScoreRankingToRedis() {
        Integer status = getActiveStatus();

        // 活动未开始不存入缓存
        if (status == ActiveStatusEnum.BEGIN.getValue()) {
            return;
        }

        // 获取记录数
        Integer count = userService.findCount();

        if (count == 0) {
            return;
        }

        final int size = 2000;
        int page = (int) Math.ceil((double) count / (double) size);
        LinkedHashMap<String, Object> rankIndexMap = new LinkedHashMap<>();

        for (int i = 0; i <= page; i++) {
            int index = i * size;
            // 查询
            List<UserRankingListVO> rankList = userScoreMapper.selectUserScoreRanking(index, size);
            for (int j = 0; j < rankList.size(); j++) {
                UserRankingListVO userScoreVO = rankList.get(j);
                Gson gson = new Gson();
                // 存储
                rankIndexMap.put(userScoreVO.getId().toString(), gson.toJson(userScoreVO));
            }
        }

        redisUtil.hmset(Configs.REDIS_ACTIVE_USER_RANK, rankIndexMap);
    }

    /**
     * 获取活动状态
     *
     * @param
     * @return java.lang.Integer
     * @author qixinyi
     * @date 2021/6/30 9:23
     */
    private Integer getActiveStatus() {
        JsonResult activeTimeJson = prizeBookService.getActiveTime(ActiveEntranceEnum.USER_SCORE.getValue());
        if (activeTimeJson.getStatus() == ResponseCode.INTERNAL_SERVER_ERROR.getCode()) {
            log.info(activeTimeJson.getMsg());
        }
        ActiveTimeVo activeTimeVo = (ActiveTimeVo) activeTimeJson.getData();
        return activeTimeVo.getStatus();
    }

    /**
     * 保存用户周榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/8 10:09
     */
    @Override
    public void saveUserScoreWeekRankingToRedis() {
        // 时间范围
        Integer status = getActiveStatus();

        // 活动未开始不存入缓存
        DatePo weekDate = null;
        if (status == ActiveStatusEnum.BEGIN.getValue()) {
            return;
        } else if (status == ActiveStatusEnum.ING.getValue()) {
            // 活动中
            weekDate = weekRankingUtil.getWeekDate();
        } else {
            // 活动结束 获取最后一周时间
            weekDate = weekRankingUtil.getLastWeek();
        }

        // 获取记录数
        Integer count = userService.findCount();

        if (count == 0) {
            return;
        }

        final int size = 2000;
        int page = (int) Math.ceil((double) count / (double) size);

        if (weekDate != null) {
            LinkedHashMap<String, Object> rankIndexMap = new LinkedHashMap<>();
            for (int i = 0; i <= page; i++) {
                int index = i * size;
                // 查询
                List<UserRankingListVO> rankList = userScoreMapper.selectUserScoreWeekRanking(index, size, weekDate.getBeginDate(), weekDate.getEndDate());
                for (int j = 0; j < rankList.size(); j++) {
                    UserRankingListVO userScoreVO = rankList.get(j);
                    Gson gson = new Gson();
                    // 存储
                    rankIndexMap.put(userScoreVO.getId().toString(), gson.toJson(userScoreVO));
                }
            }

            redisUtil.hmset(Configs.REDIS_ACTIVE_USER_WEEK_RANK, rankIndexMap);
        }
    }

    /**
     * 保存答题总榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/9 10:07
     */
    @Override
    public void saveAnswerScoreRankingToRedis() {
        Integer status = getActiveStatus();

        // 活动未开始不存入缓存
        if (status == ActiveStatusEnum.BEGIN.getValue()) {
            return;
        }

        // 获取记录数
        Integer count = userService.findCount();

        if (count == 0) {
            return;
        }

        final int size = 2000;
        int page = (int) Math.ceil((double) count / (double) size);
        LinkedHashMap<String, Object> rankIndexMap = new LinkedHashMap<>();

        for (int i = 0; i <= page; i++) {
            int index = i * size;
            // 查询
            List<UserRankingListVO> rankList = userScoreMapper.selectAnswerScoreRanking(index, size);

            if (rankList != null && rankList.size() > 0) {
                for (int j = 0; j < rankList.size(); j++) {
                    UserRankingListVO userScoreVO = rankList.get(j);
                    Gson gson = new Gson();
                    // 存储
                    rankIndexMap.put(userScoreVO.getId().toString(), gson.toJson(userScoreVO));
                }
            }
        }

        redisUtil.hmset(Configs.REDIS_ACTIVE_ANSWER_RANK, rankIndexMap, Configs.REDIS_ACTIVE_RANK_TIME_OUT);
    }

    /**
     * 获取用户积分
     *
     * @param
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.userScore.UserScoreTotalOfDayVO>
     * @author qixinyi
     * @date 2021/6/10 16:10
     */
    @Override
    public JsonResult<UserScoreTotalVO> findScoreTotalOfDay() {
        UserVO loginUser = loginUserUtil.getLoginUser();

        if (!StringUtils.isEmpty(loginUser)) {
            UserScoreTotalVO userScoreTotalOfDayVO = userScoreMapper.selectScoreTotalOfDay(loginUser.getId());

            int teamId = loginUser.getTeamId();

            // 封装是否加入团队字段
            if (teamId > 0) {
                // 封装团队名
                Team team = teamService.findTeamById(teamId);
                userScoreTotalOfDayVO.setTeamName(team.getName());

                userScoreTotalOfDayVO.setIsJoinTeam(true);
            } else {
                userScoreTotalOfDayVO.setIsJoinTeam(false);
            }

            return JsonResult.ok(userScoreTotalOfDayVO);
        }

        return JsonResult.fail("请先登录");
    }

    /**
     * 加积分
     *
     * @param type     积分类型
     * @param targetId 关联id(source_type为时1 党历史id 3 答题编号 4分享记录id 6 奖品id 7时为团队id)
     * @param score    积分数
     * @return com.yitu.cpcFounding.api.vo.JsonResult<java.lang.String>
     * @author qixinyi
     * @date 2021/6/16 18:46
     */
    @Override
    //@Transactional
    public JsonResult<String> addScore(Integer type, Long targetId, Integer score) {
        // 校验
        if (!EnumUtil.isExist(SourceTypeEnum.class, type) || type == SourceTypeEnum.DAY_ANSWER.getValue()) {
            return JsonResult.fail("加积分类型码数据非法");
        }

        UserVO loginUser = loginUserUtil.getLoginUser();

        if (loginUser != null) {
            // 校验是否认证
            Long id = loginUser.getId();
            User user = userService.findUserById(id);
            if (user.getMemberState() == MemberStateEnum.YEAR_FESTIVAL.getCode()) {
                return JsonResult.fail("非实名会员，请认证");
            }

            // 校验关联id
            if (type == SourceTypeEnum.READ_ARTICLE.getValue() || type == SourceTypeEnum.JOIN_TEAM.getValue()) {
                if (targetId == 0) {
                    return JsonResult.fail("关联id不能为空");
                }
            }

            // 校验活动时间
            JsonResult activeTimeJson = prizeBookService.getActiveTime(ActiveEntranceEnum.USER_SCORE.getValue());
            if (activeTimeJson.getStatus() == ResponseCode.INTERNAL_SERVER_ERROR.getCode()) {
                return JsonResult.fail(activeTimeJson.getMsg());
            }
            ActiveTimeVo activeTimeVo = (ActiveTimeVo) activeTimeJson.getData();

            if (activeTimeVo.getStatus() == ActiveStatusEnum.BEGIN.getValue()) {
                return JsonResult.fail("活动未开始");
            } else if (activeTimeVo.getStatus() == ActiveStatusEnum.END.getValue()) {
                return JsonResult.fail("活动已结束");
            }

            if (!StringUtils.isEmpty(loginUser) && user.getMemberState() == MemberStateEnum.RULES.getCode()) {
                Boolean flag = true;

                // 封装数据
                UserScore userScore = new UserScore();
                userScore.setAddUser(user.getWxUserName());
                userScore.setAddDate(new Date());
                userScore.setDeleted(DeletedEnum.NOT_DELETE.getValue());
                userScore.setIp(IpUtil.getIpAdrress(request));
                userScore.setPhone(user.getPhone());
                userScore.setTeamId(Long.parseLong(user.getTeamId() + ""));
                userScore.setUserId(id);
                userScore.setWxUserName(user.getWxUserName());

                if (SourceTypeEnum.SIGIN_IN.getValue() == type) {
                    // 签到 每日首次登陆 一次积分5分 上限5分
                    Integer sum = userScoreMapper.selectSumOfDayByScoreTypeAndUserId(SourceTypeEnum.SIGIN_IN.getValue(), id);
                    if (sum != null && sum >= SourceTypeNumEnum.SIGN_IN_COUNT.getName()) {
                        return JsonResult.fail("今日签到可获取积分已达上限");
                    }

                    userScore.setScore(SourceTypeNumEnum.fromValue(SourceTypeEnum.SIGIN_IN.getValue()));
                    userScore.setSourceType(SourceTypeEnum.SIGIN_IN.getValue());

                    // 加入消息队列，添加积分明细
                    flag = sendToMessageQueue(userScore);

                    if (!flag) {
                        return JsonResult.fail("签到失败");
                    }

                    return JsonResult.ok("签到成功");
                } else if (SourceTypeEnum.READ_ARTICLE.getValue() == type) { //
                    // 阅读文章 阅读10s 一次积分5分 上限50分
                    // 校验 用户是否达上限
                    Integer sum = userScoreMapper.selectSumOfDayByScoreTypeAndUserId(SourceTypeEnum.READ_ARTICLE.getValue(), id);
                    if (sum != null && sum >= SourceTypeNumEnum.READ_ARTICLE_COUNT.getName()) {
                        return JsonResult.fail("今日阅读文章可获取积分已达上限");
                    }

                    userScore.setScore(SourceTypeNumEnum.fromValue(SourceTypeEnum.READ_ARTICLE.getValue()));
                    userScore.setSourceType(SourceTypeEnum.READ_ARTICLE.getValue());
                    userScore.setTargetId(targetId.toString());

                    // 加入消息队列，添加积分明细
                    flag = sendToMessageQueue(userScore);
                } else if (SourceTypeEnum.DAY_FIRST_SHARE.getValue() == type) { //
                    // 每日首次分享 每日首次分享 一次积分5分 上限5分
                    Integer sum = userScoreMapper.selectSumOfDayByScoreTypeAndUserId(SourceTypeEnum.DAY_FIRST_SHARE.getValue(), id);
                    if (sum != null && sum >= SourceTypeNumEnum.DAY_FIRST_SHARE_COUNT.getName()) {
                        return JsonResult.fail("每日首次分享可获取积分已达上限");
                    }

                    userScore.setScore(SourceTypeNumEnum.fromValue(SourceTypeEnum.DAY_FIRST_SHARE.getValue()));
                    userScore.setSourceType(SourceTypeEnum.DAY_FIRST_SHARE.getValue());

                    // 加入消息队列，添加积分明细
                    flag = sendToMessageQueue(userScore);

                    if (!flag) {
                        return JsonResult.fail("分享失败");
                    }

                    return JsonResult.ok("分享成功");
                } else if (SourceTypeEnum.INVITE_FRIEND.getValue() == type) {
                    // 校验
                    // 邀请好友完成答题 邀请人 一次积分20分 上限100分
                    Integer sum = userScoreMapper.selectSumOfDayByScoreTypeAndUserId(SourceTypeEnum.INVITE_FRIEND.getValue(), id);
                    if (sum != null && sum >= SourceTypeNumEnum.INVITE_FRIEND_COUNT.getName()) {
                        return JsonResult.fail("今日邀请好友完成答题可获取积分已达上限");
                    }

                    userScore.setScore(SourceTypeNumEnum.fromValue(SourceTypeEnum.INVITE_FRIEND.getValue()));
                    userScore.setSourceType(SourceTypeEnum.INVITE_FRIEND.getValue());

                    // 加入消息队列，添加积分明细
                    flag = sendToMessageQueue(userScore);
                } else if (SourceTypeEnum.REWARD.getValue() == type) {
                    // 奖励得分
                    userScore.setScore(score);
                    userScore.setSourceType(SourceTypeEnum.REWARD.getValue());

                    // 加入消息队列，添加积分明细
                    flag = sendToMessageQueue(userScore);
                } else if (SourceTypeEnum.JOIN_TEAM.getValue() == type) { //
                    // 加入团队
                    // 校验是否已加入团队
                    /*LambdaQueryWrapper<UserScore> wrapper=new LambdaQueryWrapper();
                    wrapper.eq(UserScore::getUserId,id);
                    wrapper.eq(UserScore::getDeleted, DeletedEnum.NOT_DELETE);
                    wrapper.eq(UserScore::getSourceType, SourceTypeEnum.JOIN_TEAM.getValue());

                    UserScore JoinTeamScore=userScoreMapper.selectOne(wrapper);*/

                    if (loginUser.getTeamId() > 0) {
                        return JsonResult.fail("已加入团队，无法重复积分");
                    }

                    userScore.setScore(SourceTypeNumEnum.fromValue(SourceTypeEnum.JOIN_TEAM.getValue()));
                    userScore.setSourceType(SourceTypeEnum.JOIN_TEAM.getValue());
                    userScore.setTargetId(targetId.toString());

                    // 加入消息队列，添加积分明细
                    flag = sendToMessageQueue(userScore);
                } else if (SourceTypeEnum.ROTARY_DRAW.getValue() == type) {
                    // todo 转盘抽奖

                }

                if (!flag) {
                    return JsonResult.fail("加积分失败");
                }
            }

            return JsonResult.ok();
        }

        return JsonResult.fail("请先登录");
    }

    /**
     * 发送消息到队列
     *
     * @param userScore 用户积分实体对象
     * @return java.lang.Boolean
     * @author qixinyi
     * @date 2021/6/21 9:41
     */
    private Boolean sendToMessageQueue(UserScore userScore) {
        try {
            Gson gson = new Gson();
            String userScoreData = gson.toJson(userScore);
            rabbitmqProducer.sendMessage(QueueEnum.USER_SCORE_QUEUE, userScoreData);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**
     * 获取每日积分明细
     *
     * @param
     * @return com.yitu.cpcFounding.api.vo.JsonResult<UserScoresOfDay>
     * @author qixinyi
     * @date 2021/6/11 16:14
     */
    @Override
    public JsonResult<UserScoresOfDay> findUserScoresOfDay() {
        UserVO loginUser = loginUserUtil.getLoginUser();

        UserScoresOfDay userScoresOfDay = new UserScoresOfDay();
        if (!StringUtils.isEmpty(loginUser)) {
            Long id = loginUser.getId();

            // 封装数据
            userScoresOfDay.setId(id);

            // 签到积分
            Integer signIdSum = userScoreMapper.selectSumOfDayByScoreTypeAndUserId(SourceTypeEnum.SIGIN_IN.getValue(), id);
            userScoresOfDay.setSignInScore(signIdSum);

            // 阅读文章积分
            Integer readArticleSum = userScoreMapper.selectSumOfDayByScoreTypeAndUserId(SourceTypeEnum.READ_ARTICLE.getValue(), id);
            userScoresOfDay.setReadArticleScore(readArticleSum);

            // 每日答题积分
            Integer answerSum = userScoreMapper.selectSumOfDayByScoreTypeAndUserId(SourceTypeEnum.DAY_ANSWER.getValue(), id);
            userScoresOfDay.setAnswerScore(answerSum);

            // 分享积分
            Integer shareSum = userScoreMapper.selectSumOfDayByScoreTypeAndUserId(SourceTypeEnum.DAY_FIRST_SHARE.getValue(), id);
            userScoresOfDay.setShareScore(shareSum);

            // 邀请好友积分
            Integer inviteSum = userScoreMapper.selectSumOfDayByScoreTypeAndUserId(SourceTypeEnum.INVITE_FRIEND.getValue(), id);
            userScoresOfDay.setInviteScore(inviteSum);

            //是否加入团队
            if (loginUser.getTeamId() > 0) {
                userScoresOfDay.setUserTeamScore(SourceTypeNumEnum.JOIN_TEAM.getName());
            } else {
                userScoresOfDay.setUserTeamScore(0);
            }

            return JsonResult.ok(userScoresOfDay);
        }

        return JsonResult.fail("请先登录");
    }

    /**
     * 每日首次分享和签到加积分
     *
     * @param sourceType 积分来源类型
     * @return com.yitu.cpcFounding.api.vo.JsonResult
     * @author qixinyi
     * @date 2021/6/18 17:16
     */
    @Override
    //@Transactional
    public JsonResult addShareAndSignInScore(Integer sourceType) {
        // 校验
        if (sourceType != SourceTypeEnum.DAY_FIRST_SHARE.getValue() && sourceType != SourceTypeEnum.SIGIN_IN.getValue()) {
            return JsonResult.fail("积分来源类型数据码非法");
        }

        Map map = new HashMap();
        JsonResult<String> result = addScore(sourceType, null, null);

        // 失败
        if (result.getStatus() == ResponseCode.INTERNAL_SERVER_ERROR.getCode()) {
            map.put("scoreStatus", 0);
            map.put("scoreStr", result.getMsg());
            return JsonResult.ok(map);
        }

        // 成功
        map.put("scoreStatus", 1);
        map.put("scoreStr", "积分：+5");
        return JsonResult.ok(map);
    }

    /**
     * 获取排行榜提示语
     *
     * @param rankingType
     * @return com.yitu.cpcFounding.api.vo.userScore.UserScoreLabelVO
     * @author wangping
     * @date 2021/6/28 15:47
     */
    @Override
    public UserScoreLabelVO getRankingLabel(int rankingType) {
        List<Config> configList = configService.getConfigListByType(ConfigEnum.RANKING_LABEL.getCode());
        RankingLabelEnum entranceEnum = RankingLabelEnum.parse(rankingType);
        if (entranceEnum == null) {
            log.error("类型有误");
        }
        UserScoreLabelVO userScoreLabelVO = new UserScoreLabelVO();
        String keyId = String.valueOf(entranceEnum.getValue());
        List<Config> selectList = configList.stream().filter(k -> k.getKeyId().equals(keyId)).collect(Collectors.toList());
        if (selectList.size() > 0) {
            Gson gson = new Gson();
            userScoreLabelVO = gson.fromJson(selectList.get(0).getKeyValue(), new TypeToken<UserScoreLabelVO>() {
            }.getType());
        }

        return userScoreLabelVO;
    }

    /**
     * 获取每日次数明细
     *
     * @param
     * @return com.yitu.cpcFounding.api.vo.JsonResult<UserScoresOfDay>
     * @author qixinyi
     * @date 2021/6/11 16:14
     */
    @Override
    public JsonResult<UserScoreTimesOfDay> findUserScoreTimesOfDay() {
        UserVO loginUser = loginUserUtil.getLoginUser();

        UserScoreTimesOfDay userScoreTimesOfDay = new UserScoreTimesOfDay();
        if (loginUser != null) {
            Long id = loginUser.getId();
            userScoreTimesOfDay.setId(id);
            int teamId = loginUser.getTeamId();
            String sessionId = loginUser.getSessionId();

            // 签到次数
            Integer signInTimes = userScoreMapper.selectTimesOfDayByTypeAndUserId(SourceTypeEnum.SIGIN_IN.getValue(), id);

            // 阅读文章次数
            Integer readTimes = userScoreMapper.selectTimesOfDayByTypeAndUserId(SourceTypeEnum.READ_ARTICLE.getValue(), id);

            // 每日答题次数
//            Integer answerTimes=userAnswerMapper.selectTimesOfDayByUserId(id);
            DynamicUserVO userDynamicInfo = commonService.getUserDynamicInfo(id, sessionId);
            int answerCount = userDynamicInfo.getAnswerCount();

            // 分享次数
            Integer shareTimes = userScoreMapper.selectTimesOfDayByTypeAndUserId(SourceTypeEnum.DAY_FIRST_SHARE.getValue(), id);

            // 邀请好友次数
            Integer inviteTimes = userScoreMapper.selectTimesOfDayByTypeAndUserId(SourceTypeEnum.INVITE_FRIEND.getValue(), id);

            // 加入团队次数
            if (teamId > 0) {
                userScoreTimesOfDay.setUserTeamTimes(1);
            } else {
                userScoreTimesOfDay.setUserTeamTimes(0);
            }

            userScoreTimesOfDay.setSignInTimes(signInTimes);
            userScoreTimesOfDay.setReadArticleTimes(readTimes);
            userScoreTimesOfDay.setAnswerTimes(answerCount);
            userScoreTimesOfDay.setShareTimes(shareTimes);
            userScoreTimesOfDay.setInviteTimes(inviteTimes);
        } else {
            return JsonResult.fail("请先登录");
        }

        return JsonResult.ok(userScoreTimesOfDay);
    }
}
