package com.yitu.cpcFounding.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yitu.cpcFounding.api.auth.LoginUserUtil;
import com.yitu.cpcFounding.api.constant.Configs;
import com.yitu.cpcFounding.api.domain.Team;
import com.yitu.cpcFounding.api.domain.User;
import com.yitu.cpcFounding.api.enums.*;
import com.yitu.cpcFounding.api.mapper.TeamMapper;
import com.yitu.cpcFounding.api.mapper.UserMapper;
import com.yitu.cpcFounding.api.po.DatePo;
import com.yitu.cpcFounding.api.redis.RedisUtil;
import com.yitu.cpcFounding.api.service.CommonService;
import com.yitu.cpcFounding.api.service.PrizeBookService;
import com.yitu.cpcFounding.api.service.TeamService;
import com.yitu.cpcFounding.api.service.UserScoreService;
import com.yitu.cpcFounding.api.utils.*;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.TeamScoreVO;
import com.yitu.cpcFounding.api.vo.UserVO;
import com.yitu.cpcFounding.api.vo.prizeBook.ActiveTimeVo;
import com.yitu.cpcFounding.api.vo.team.*;
import com.yitu.cpcFounding.api.vo.userScore.UserListVO;
import com.yitu.cpcFounding.api.vo.userScore.UserRankingListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 团队服务实现
 *
 * @author wangping
 * @date 2021-04-09
 */
@Service
@Slf4j
public class TeamServiceImpl implements TeamService {
    @Resource
    private TeamMapper teamMapper;
    @Resource
    private LoginUserUtil loginUserUtil;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private CommonService commonService;
    @Autowired
    private PrizeBookService prizeBookService;
    @Resource
    private RedisUtil redisUtil;
    @Autowired
    private WeekRankingUtil weekRankingUtil;
    @Resource
    UserScoreService userScoreService;
    //加入团队锁
    private Lock teamLock = new ReentrantLock();

    /**
     * 获取团队列表以及当前用户所属团队数据（分页）
     * 排序类型和团队名称查找 --MedalTotal根据奖章排序，团队榜,peopleTotal根据总人数排序
     *
     * @param name      团队名称
     * @param pageIndex 页索引
     * @param pageSize  页大小
     * @return 获奖团队表列表实体
     * @author wangping
     * @date 2021-04-09 15:02:18
     */
    @Override
    public JsonResult<UserTeamListVO> getTeamList(String name, int pageIndex, int pageSize, Integer type) {
        UserVO loginUser = loginUserUtil.getLoginUser();
        UserTeamListVO userTeamVO = new UserTeamListVO();
        //团队列表信息
        Page<Team> page = new Page<>(pageIndex, pageSize);
        LambdaQueryWrapper<Team> wrapper = new LambdaQueryWrapper();
        wrapper.select(Team::getId, Team::getName, Team::getScoreTotal, Team::getPeopleTotal, Team::getPrizeStatus);//查询部分字段
        wrapper.eq(Team::getDeleted, DeletedEnum.NOT_DELETE.getValue());

        if (type != null && type > 0) {
            wrapper.eq(Team::getType, type);
        } else {
            wrapper.eq(Team::getType, TeamTypeEnum.TEAM.getValue());
        }

        if (!StringUtil.isEmpty(name)) {
            wrapper.like(Team::getName, name);
        }

        wrapper.orderByDesc(Team::getPeopleTotal);
        wrapper.orderByDesc(Team::getAddDate);
        IPage<Team> resultPage = teamMapper.selectPage(page, wrapper);
        //数据对象转换
        IPage<TeamInfoVO> resultPageList = resultPage.convert(operationLog -> BeanCopyUtil.of(operationLog, new TeamInfoVO())
                .copy(BeanUtils::copyProperties).get());
        userTeamVO.setTopList(resultPageList);
        //当前用户团队信息
        if (!StringUtils.isEmpty(loginUser) && loginUser.getTeamId() > 0) {
            userTeamVO.setUserStatus(true);
        } else {
            userTeamVO.setUserStatus(false);
        }

        return JsonResult.ok(userTeamVO);
    }

    /**
     * 获取个人列表以及当前用户所属数据，根据奖章排序，个人榜（分页）
     * 只查询工会会员进行排序
     *
     * @param pageIndex 页索引
     * @param pageSize  页大小
     * @return 获奖个人列表
     * @author wangping
     * @date 2021-04-09 15:02:18
     */
    @Override
    public JsonResult<UserListVO> getUserList(int pageIndex, int pageSize, String wxUserName) {
        UserVO loginUser = loginUserUtil.getLoginUser();
        UserListVO userListVO = new UserListVO();
        //个人列表信息
        Page<User> page = new Page<>(pageIndex, pageSize);

        IPage<User> resultPage = userMapper.userPageList(page, wxUserName);
        //数据对象转换
        IPage<UserRankingListVO> resultPageList = resultPage.convert(operationLog -> BeanCopyUtil.of(operationLog, new UserRankingListVO())
                .copy(BeanUtils::copyProperties).get());
        userListVO.setTopList(resultPageList);
        //当前用户信息
        if (!StringUtils.isEmpty(loginUser)) {
            //获取当前用户以及是否获奖
//            UserMedalVO userMedalVO = userMedalMapper.queryByIdPrize(loginUser.getId());
//            //获取当前用户奖章排名
//            Map map=redisUtil.hmget(Configs.REDIS_ACTIVE_RANK);
//            if(!StringUtils.isEmpty(map.get(loginUser.getId().toString()))){
//                userMedalVO.setUserSort((Integer) map.get(loginUser.getId().toString()));
//            }
//            userListVO.setSelf(userMedalVO);

            if (MemberState.Member.getCode() == loginUser.getMemberState()) {
                userListVO.setUserState(true);
            } else {
                userListVO.setUserState(false);
            }
        } else {
            userListVO.setUserState(false);
        }

        return JsonResult.ok(userListVO);
    }

    /**
     * 用户加入团队
     *
     * @param teamId
     * @return com.yitu.mayday.api.vo.JsonResult
     * @author wangping
     * @date 2021/4/9 15:56
     */
    @Override
    public JsonResult userAddTeam(int teamId) {
        //活动开始才能加入团队
        JsonResult activeTimeJson = prizeBookService.getActiveTime(ActiveEntranceEnum.JION_TEAM.getValue());
        if (activeTimeJson.getStatus() == ResponseCode.INTERNAL_SERVER_ERROR.getCode()) {
            return JsonResult.fail(activeTimeJson.getMsg());
        }
        ActiveTimeVo activeTimeVo = (ActiveTimeVo) activeTimeJson.getData();

        if (activeTimeVo.getStatus() == ActiveStatusEnum.BEGIN.getValue()) {
            return JsonResult.fail("活动未开始");
        } else if (activeTimeVo.getStatus() == ActiveStatusEnum.END.getValue()) {
            return JsonResult.fail("活动已结束");
        }

        Team team = teamMapper.selectById(teamId);
        if (StringUtils.isEmpty(team) || team.getDeleted() != 0 || team.getType() != 3) {
            return JsonResult.fail("团队不存在");
        }

        //获取当前用户
        UserVO loginUser = loginUserUtil.getLoginUser();
        User user = userMapper.selectById(loginUser.getId());
        if (MemberState.Member.getCode() != user.getMemberState()) {
            return JsonResult.fail("非实名用户不能加入团队");
        }

        if (user.getTeamId() > 0) {
            return JsonResult.fail("您已加入团队，不能重复加入");
        }

        teamLock.lock();
        try {
            if (redisUtil.hasKey(Configs.TEAM + user.getId())) {
                return JsonResult.fail("您已加入团队，不能重复加入");
            }
            redisUtil.set(Configs.TEAM + user.getId(), teamId);
        }finally {
            teamLock.unlock();
        }

        //加入团队
        user.setTeamId(teamId);
        int userCode = userMapper.updateById(user);
        if (userCode == 0) {
            throw new RuntimeException("系统繁忙，请稍后再试!");
        }

        //修改团队积分-和总人数
        /*team.setScoreTotal(team.getScoreTotal() + user.getScoreTotal());
        team.setPeopleTotal(team.getPeopleTotal() + 1);
        int teamCode = teamMapper.updateById(team);*/

        //修改团队积分和总人数,因加入团队个人会得50分，所以这边加团队直接多加50分
        int teamCode = teamMapper.updateTeamScorePeople(team.getId(), user.getScoreTotal(), SourceTypeNumEnum.JOIN_TEAM.getName());
        if (teamCode <= 0) {
            throw new RuntimeException("系统繁忙，请稍后再试!");
        }

        //修改街道积分
        if (team.getParentId() > 0) {
            int teamCode2 = teamMapper.updateTeamScorePeople(team.getParentId(), user.getScoreTotal(), SourceTypeNumEnum.JOIN_TEAM.getName());
            if (teamCode2 <= 0) {
                throw new RuntimeException("系统繁忙，请稍后再试!");
            }
            // 片区加积分
            Team areaTeam = teamMapper.selectById(team.getParentId());
            if (areaTeam != null) {
                if (areaTeam.getParentId() > 0) {
                    int teamCode3 = teamMapper.updateTeamScorePeople(areaTeam.getParentId(), user.getScoreTotal(), SourceTypeNumEnum.JOIN_TEAM.getName());
                    if (teamCode3 <= 0) {
                        throw new RuntimeException("系统繁忙，请稍后再试!");
                    }
                }
            }
        }

        //修改加入团队后给用户加积分等后续操作
        //加入积分逻辑
        JsonResult addScoreJson = userScoreService.addScore(SourceTypeEnum.JOIN_TEAM.getValue(), team.getId(), null);
        if (addScoreJson.getStatus() == ResponseCode.INTERNAL_SERVER_ERROR.getCode()) {
            return JsonResult.fail(addScoreJson.getMsg());
        }

        //加入团队成功后刷新缓存
        loginUser.setTeamId(teamId);
        commonService.setLoginUser(loginUser.getSessionId(), loginUser);
        TeamVO teamVO = BeanCopyUtil.of(team, new TeamVO()).copy(BeanUtils::copyProperties).get();
        return JsonResult.ok(teamVO);
    }

    /**
     * 获取团队信息详情以及团队下的用户所拥有的积分排行（分页）
     *
     * @param pageIndex 页索引
     * @param pageSize  页大小
     * @param teamId    团队id
     * @return com.yitu.mayday.api.vo.JsonResult<com.yitu.mayday.api.vo.userMedal.TeamUserListVO>
     * @author wangping
     * @date 2021/4/10 10:04
     */
    @Override
    public JsonResult<TeamUserListVO> getTeamById(int teamId, int pageIndex, int pageSize) {
        TeamUserListVO teamUserListVO = new TeamUserListVO();
        //团队信息
        Team team = teamMapper.selectById(teamId);
        if (StringUtils.isEmpty(team) || team.getDeleted() != 0) {
            return JsonResult.fail("团队不存在");
        }

        TeamVO teamVO = BeanCopyUtil.of(team, new TeamVO()).copy(BeanUtils::copyProperties).get();

        //获取当前用户
        UserVO loginUser = loginUserUtil.getLoginUser();
        if (!StringUtils.isEmpty(loginUser)) {
            if (loginUser.getTeamId() > 0) {
                teamUserListVO.setUserStatus(true);//是否加入团队
                if (teamVO.getId() == loginUser.getTeamId()) {
                    teamVO.setUserTeamStatus(1);//是否所属当前团队
                } else {
                    teamVO.setUserTeamStatus(0);
                }
            } else {
                teamVO.setUserTeamStatus(0);
                teamUserListVO.setUserStatus(false);
            }
        } else {
            teamVO.setUserTeamStatus(0);
            teamUserListVO.setUserStatus(false);
        }

        teamUserListVO.setSelf(teamVO);

        Page<User> page = new Page<>(pageIndex, pageSize);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper();
        wrapper.select(User::getId, User::getWxUserName, User::getScoreTotal, User::getWxHeadPic);//查询部分字段
        wrapper.eq(User::getDeleted, DeletedEnum.NOT_DELETE.getValue());
        wrapper.eq(User::getTeamId, team.getId());
        wrapper.orderByDesc(User::getScoreTotal);
        IPage<User> resultPage = userMapper.selectPage(page, wrapper);
        //数据对象转换
        IPage<UserRankingListVO> resultPageList = resultPage.convert(operationLog -> BeanCopyUtil.of(operationLog, new UserRankingListVO())
                .copy(BeanUtils::copyProperties).get());
        teamUserListVO.setTopList(resultPageList);

        return JsonResult.ok(teamUserListVO);
    }

    /**
     * 获取团队总榜/周榜分页数据
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    private IPage<TeamRankingListVO> getTeamRankingPage(int pageIndex, int pageSize,RankingEnum rankingEnum,TeamTypeEnum teamTypeEnum) {
        //大于缓存条数时，直接从数据库取
        if (pageIndex > Configs.pageIndexRedis) {
            return getTeamRankingListPage(pageIndex, pageSize,rankingEnum,teamTypeEnum);
        }
        String key = "";
        switch (rankingEnum){
            case TOTAL_RANKING:
                switch (teamTypeEnum){
                    case TEAM:
                        key = Configs.RANKING_PAGE_TEAM_TOTAL;
                        break;
                    case STREET:
                        key = Configs.RANKING_PAGE_STREET_TOTAL;
                        break;
                    case AREA:
                        key = Configs.RANKING_PAGE_AREA_TOTAL;
                        break;
                }
                break;
            case WEEK_RANKING:
                switch (teamTypeEnum){
                    case TEAM:
                        key = Configs.RANKING_PAGE_TEAM_WEEK;
                        break;
                    case STREET:
                        key = Configs.RANKING_PAGE_STREET_WEEK;
                        break;
                    case AREA:
                        key = Configs.RANKING_PAGE_AREA_WEEK;
                        break;
                }
            default:
                break;
        }
        Gson gson = new Gson();
        boolean hasKey = redisUtil.hasKey(key);
        if (hasKey) {
            //直接从缓存中取
            Object obj = redisUtil.hget(key, pageIndex + "");
            Type type = new TypeToken<Page<TeamRankingListVO>>() {
            }.getType();
            IPage<TeamRankingListVO> pages = gson.fromJson(obj.toString(), type);
            return pages;
        }
        return null;
    }

    /**
     * 获取团队总榜/周榜排名分页
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    private IPage<TeamRankingListVO> getTeamRankingListPage(int pageIndex, int pageSize,RankingEnum rankingEnum,TeamTypeEnum teamTypeEnum) {
        Page<TeamRankingListVO> page = new Page<>(pageIndex, pageSize);
        IPage<TeamRankingListVO> pages = null;
        switch (rankingEnum){
            case TOTAL_RANKING:
                pages = teamMapper.selectTeamScoreRankings(page,teamTypeEnum.getValue());
                break;
            case WEEK_RANKING:
                pages=teamMapper.selectTeamScoreWeekRankings(page,teamTypeEnum.getValue());
            default:
                break;
        }

        //处理排名
        List<TeamRankingListVO> records = pages.getRecords();
        if(records!=null && records.size()>0){
            for (int i = 0; i < records.size(); i++) {
                records.get(i).setTeamSort(pageSize*(pageIndex-1)+1+i);
            }
        }
        pages.setRecords(records);
        return pages;
    }

    /**
     * 获取团队榜
     *
     * @param type      排行榜类型
     * @param pageSize  显示条数
     * @param pageIndex 页码
     * @param teamType  类型
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.TeamScoreVO>
     * @author qixinyi
     * @date 2021/6/16 18:00
     */
    @Override
    public JsonResult<TeamScoreVO> findScoreSumByType(Integer type, int pageIndex, int pageSize, int teamType) {
        UserVO loginUser = loginUserUtil.getLoginUser();

        // 校验
        if (!EnumUtil.isExist(RankingEnum.class, type)) {
            return JsonResult.fail("排行榜类型状态码数据非法");
        }

        // 校验
        if (!EnumUtil.isExist(TeamTypeEnum.class, teamType)) {
            return JsonResult.fail("类型状态码数据非法");
        }

        JsonResult activeTimeJson = prizeBookService.getActiveTime(ActiveEntranceEnum.USER_SCORE.getValue());
        if (activeTimeJson.getStatus() == ResponseCode.INTERNAL_SERVER_ERROR.getCode()) {
            return JsonResult.fail(activeTimeJson.getMsg());
        }
        ActiveTimeVo activeTimeVo = (ActiveTimeVo) activeTimeJson.getData();
        Integer status = activeTimeVo.getStatus();

        // 封装
        TeamScoreVO teamScoreVO = new TeamScoreVO();
        if (loginUser != null) {
            Integer teamId = loginUser.getTeamId();

            // 封装个人信息
            String key = "";
            if (type==RankingEnum.TOTAL_RANKING.getValue()) {
                if(teamType==TeamTypeEnum.TEAM.getValue()){
                    key=Configs.REDIS_ACTIVE_TEAM_SORT;
                }else if(teamType==TeamTypeEnum.STREET.getValue()){
                    key=Configs.REDIS_ACTIVE_STREET_SORT;
                }else if(teamType==TeamTypeEnum.AREA.getValue()){
                    key=Configs.REDIS_ACTIVE_AREA_SORT;
                }
            }else if(type==RankingEnum.WEEK_RANKING.getValue()){
                if(teamType==TeamTypeEnum.TEAM.getValue()){
                    key=Configs.REDIS_ACTIVE_TEAM_WEEK_SORT;
                }else if(teamType==TeamTypeEnum.STREET.getValue()){
                    key=Configs.REDIS_ACTIVE_STREET_WEEK_SORT;
                }else if(teamType==TeamTypeEnum.AREA.getValue()){
                    key=Configs.REDIS_ACTIVE_AREA_WEEK_SORT;
                }
            }

            Gson gson = new Gson();
            // 判断键值是否存在
            boolean teamScoreKey = redisUtil.hHasKey(key, teamId.toString());
            if (teamScoreKey) {
                String teamScoreJson = redisUtil.hget(key, teamId.toString()).toString();
                if (!org.apache.commons.lang3.StringUtils.isEmpty(teamScoreJson)) {
                    TeamRankingListVO teamRankingListVO = gson.fromJson(teamScoreJson, TeamRankingListVO.class);
                    // 获取用户总榜排名
                    teamScoreVO.setSelf(teamRankingListVO);
                }
            }

            if (RankingEnum.TOTAL_RANKING.getValue() == type) {
                // 查询
                IPage<TeamRankingListVO> pages=getTeamRankingPage(pageIndex,pageSize,RankingEnum.TOTAL_RANKING,TeamTypeEnum.fromValue(teamType));
                teamScoreVO.setTopList(pages);

                if (teamType == TeamTypeEnum.TEAM.getValue()) {
                    //saveTeamScoreVO(pageIndex, pageSize, teamId, teamScoreVO, Configs.REDIS_ACTIVE_TEAM_RANKS, teamType, RankingTypeEnum.TEAM_RANKING.getValue());
                    //添加提示语
                    teamScoreVO.setUserScoreLabelVO(userScoreService.getRankingLabel(RankingLabelEnum.RANKING_LABEL_6.getValue()));
                } else if (teamType == TeamTypeEnum.STREET.getValue()) {
                    //saveTeamScoreVO(pageIndex, pageSize, teamId, teamScoreVO, Configs.REDIS_ACTIVE_STREET_RANK, teamType, RankingTypeEnum.STREET_RANKING.getValue());
                    //添加提示语
                    teamScoreVO.setUserScoreLabelVO(userScoreService.getRankingLabel(RankingLabelEnum.RANKING_LABEL_7.getValue()));
                } else if (teamType == TeamTypeEnum.AREA.getValue()) {
                    //saveTeamScoreVO(pageIndex, pageSize, teamId, teamScoreVO, Configs.REDIS_ACTIVE_AREA_RANK, teamType, RankingTypeEnum.AREA_RANKING.getValue());
                    //添加提示语
                    teamScoreVO.setUserScoreLabelVO(userScoreService.getRankingLabel(RankingLabelEnum.RANKING_LABEL_8.getValue()));
                }
            } else if (RankingEnum.WEEK_RANKING.getValue() == type) {
                // 查询
                IPage<TeamRankingListVO> pages=getTeamRankingPage(pageIndex,pageSize,RankingEnum.WEEK_RANKING,TeamTypeEnum.fromValue(teamType));
                teamScoreVO.setTopList(pages);

                if (teamType == TeamTypeEnum.TEAM.getValue()) {
//                    saveTeamScoreVO(pageIndex, pageSize, teamId, teamScoreVO, Configs.REDIS_ACTIVE_TEAM_WEEK_RANK, teamType, RankingTypeEnum.TEAM_WEEK_RANKING.getValue());
                    //添加提示语
                    teamScoreVO.setUserScoreLabelVO(userScoreService.getRankingLabel(RankingLabelEnum.RANKING_LABEL_3.getValue()));
                } else if (teamType == TeamTypeEnum.STREET.getValue()) {
//                    saveTeamScoreVO(pageIndex, pageSize, teamId, teamScoreVO, Configs.REDIS_ACTIVE_STREET_WEEK_RANK, teamType, RankingTypeEnum.STREET_WEEK_RANKING.getValue());
                    //添加提示语
                    teamScoreVO.setUserScoreLabelVO(userScoreService.getRankingLabel(RankingLabelEnum.RANKING_LABEL_4.getValue()));
                } else if (teamType == TeamTypeEnum.AREA.getValue()) {
//                    saveTeamScoreVO(pageIndex, pageSize, teamId, teamScoreVO, Configs.REDIS_ACTIVE_AREA_WEEK_RANK, teamType, RankingTypeEnum.AREA_WEEK_RANKING.getValue());
                    //添加提示语
                    teamScoreVO.setUserScoreLabelVO(userScoreService.getRankingLabel(RankingLabelEnum.RANKING_LABEL_5.getValue()));
                }

                DatePo weekDate = null;
                if (status == ActiveStatusEnum.ING.getValue()) {
                    weekDate = weekRankingUtil.getWeekDate();
                } else {
                    weekDate = weekRankingUtil.getLastWeek();
                }
                if (weekDate != null) {
                    String begin = weekDate.getBeginDate().substring(5, 10).replace("-", ".").replaceFirst("0", "");
                    String end = weekDate.getEndDate().substring(5, 10).replace("-", ".").replaceFirst("0", "");
                    teamScoreVO.setTime(begin + "-" + end);
                }
            }
        }

        return JsonResult.ok(teamScoreVO);
    }

    /**
     * 封装团队排行榜列表以及当前用户所属团队信息实体
     *
     * @param pageIndex   页码
     * @param pageSize    页大小
     * @param teamId      团队id
     * @param teamScoreVO 团队排行榜列表以及当前用户所属团队信息实体
     * @param key         key值
     * @param teamType    类型
     * @param type        排行榜类型
     * @return void
     * @author qixinyi
     * @date 2021/7/2 16:30
     */
    private void saveTeamScoreVO(int pageIndex, int pageSize, int teamId, TeamScoreVO teamScoreVO, String key, Integer teamType, Integer type) {
        // 封装排行榜
        Map map = redisUtil.hmget(key);

        if (map != null && map.size()>0) {
            List<TeamRankingListVO> result = (List<TeamRankingListVO>) map.values().stream().sorted().map(o -> {
                TeamRankingListVO realTeamScoreVO = JSON.parseObject(JSON.parse(o.toString()).toString(), TeamRankingListVO.class);
                return realTeamScoreVO;
            }).collect(Collectors.toList());

            //数据排序
            List<TeamRankingListVO> results = result.stream().sorted(Comparator.comparing(TeamRankingListVO::getTeamSort)).collect(Collectors.toList());

            IPage page = PageUtil.getPages(pageIndex, pageSize, results);

            teamScoreVO.setTopList(page);

            // 封装个人信息
            if (teamId > 0) {
                String userJson = null;
                TeamRankingListVO realLoginTeamRankingListVO = new TeamRankingListVO();

                if (teamType == TeamTypeEnum.TEAM.getValue()) {
                    userJson = (String) map.get(teamId + "");

                    if (userJson != null) {
                        realLoginTeamRankingListVO = JSON.parseObject(JSON.parse(userJson).toString(), TeamRankingListVO.class);

                        // 获取团队id
                        if (realLoginTeamRankingListVO != null && realLoginTeamRankingListVO.getId() == teamId) {
                            teamScoreVO.setSelf(realLoginTeamRankingListVO);
                        }
                    }
                } else if (teamType == TeamTypeEnum.STREET.getValue()) {
                    // 获取街道id
                    Team team = teamMapper.selectById(teamId);
                    Team street = teamMapper.selectById(team.getParentId());

                    userJson = (String) map.get(street.getId() + "");

                    if (userJson != null) {
                        realLoginTeamRankingListVO = JSON.parseObject(JSON.parse(userJson).toString(), TeamRankingListVO.class);

                        if (realLoginTeamRankingListVO != null && realLoginTeamRankingListVO.getId() == street.getId()) {
                            if(realLoginTeamRankingListVO.getName().indexOf("其他街道")==-1){
                                // 不包含
                                teamScoreVO.setSelf(realLoginTeamRankingListVO);
                            }
                        }
                    }
                } else if (teamType == TeamTypeEnum.AREA.getValue()) {
                    // 获取片区id
                    Team team = teamMapper.selectById(teamId);
                    Team street = teamMapper.selectById(team.getParentId());
                    Team area = teamMapper.selectById(street.getParentId());

                    userJson = (String) map.get(area.getId() + "");

                    if (userJson != null) {
                        realLoginTeamRankingListVO = JSON.parseObject(JSON.parse(userJson).toString(), TeamRankingListVO.class);

                        if (realLoginTeamRankingListVO != null && realLoginTeamRankingListVO.getId() == area.getId()) {
                            if(realLoginTeamRankingListVO.getName().indexOf("其他街道")==-1){
                                // 不包含
                                teamScoreVO.setSelf(realLoginTeamRankingListVO);
                            }
                        }
                    }
                }
            }
        }
//        else {
//            if (type == RankingTypeEnum.TEAM_RANKING.getValue()) {
//                saveTeamScoreRankingToRedis();
//            } else if (type == RankingTypeEnum.TEAM_WEEK_RANKING.getValue()) {
//                saveTeamScoreWeekRankingToRedis();
//            } else if (type == RankingTypeEnum.STREET_RANKING.getValue()) {
//                saveStreetScoreRankingToRedis();
//            } else if (type == RankingTypeEnum.STREET_WEEK_RANKING.getValue()) {
//                saveStreetScoreWeekRankingToRedis();
//            } else if (type == RankingTypeEnum.AREA_RANKING.getValue()) {
//                saveAreaScoreRankingToRedis();
//            } else if (type == RankingTypeEnum.AREA_WEEK_RANKING.getValue()) {
//                saveAreaScoreWeekRankingToRedis();
//            }
//        }
    }
    /**
     * 保存团队总榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/8 17:20
     */
    @Override
    public void saveTeamScoreRankingToRedis() {
        Integer status = getActiveStatus();

        // 活动未开始不存入缓存
        if (status == ActiveStatusEnum.BEGIN.getValue()) {
            return;
        }

        // 获取团队记录数
        Integer count = teamMapper.selectCount(new LambdaQueryWrapper<Team>()
                .eq(Team::getDeleted, DeletedEnum.NOT_DELETE.getValue())
                .eq(Team::getType, TeamTypeEnum.TEAM.getValue())
        );

        if (count == 0) {
            return;
        }

        final int size = 2000;
        int page = (int) Math.ceil((double) count / (double) size);
        LinkedHashMap<String, Object> rankIndexMap = new LinkedHashMap<>();

        // 团队
        for (int i = 0; i <= page; i++) {
            int index = i * size;
            // 查询
            List<TeamRankingListVO> rankList = teamMapper.selectTeamScoreRanking(index, size, TeamTypeEnum.TEAM.getValue());

            for (int i2 = 0; i2 < rankList.size(); i2++) {
                TeamRankingListVO teamRankingListVO = rankList.get(i2);
                Gson gson = new Gson();
                // 存储
                rankIndexMap.put(teamRankingListVO.getId().toString(), gson.toJson(teamRankingListVO));
            }
        }

        redisUtil.hmset(Configs.REDIS_ACTIVE_TEAM_RANKS, rankIndexMap);
    }

    /**
     * 保存团队周榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/9 18:39
     */
    @Override
    public void saveTeamScoreWeekRankingToRedis() {
        // 时间范围
        Integer status = getActiveStatus();

        // 活动未开始不存入缓存
        if (status == ActiveStatusEnum.BEGIN.getValue()) {
            return;
        }

        // 获取团队记录数
        Integer count = teamMapper.selectCount(new LambdaQueryWrapper<Team>()
                .eq(Team::getDeleted, DeletedEnum.NOT_DELETE.getValue())
                .eq(Team::getType, TeamTypeEnum.TEAM.getValue())
        );

        DatePo weekDate = null;
        if (count == 0) {
            return;
        } else if (status == ActiveStatusEnum.ING.getValue()) {
            // 活动中
            weekDate = weekRankingUtil.getWeekDate();
        } else {
            // 活动结束 获取最后一周时间
            weekDate = weekRankingUtil.getLastWeek();
        }

        final int size = 2000;
        int page = (int) Math.ceil((double) count / (double) size);
        LinkedHashMap<String, Object> rankIndexMap = new LinkedHashMap<>();

        if (weekDate != null) {
            // 团队
            for (int i = 0; i <= page; i++) {
                int index = i * size;
                // 查询
                List<TeamRankingListVO> rankList = teamMapper.selectStreetAndAreaScoreWeekRanking(index, size, TeamTypeEnum.TEAM.getValue());
                for (int i2 = 0; i2 < rankList.size(); i2++) {
                    TeamRankingListVO teamRankingListVO = rankList.get(i2);
                    Gson gson = new Gson();
                    // 存储
                    rankIndexMap.put(teamRankingListVO.getId().toString(), gson.toJson(teamRankingListVO));
                }
            }

            redisUtil.hmset(Configs.REDIS_ACTIVE_TEAM_WEEK_RANK, rankIndexMap);
        }
    }

    /**
     * 保存街道总榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/30 9:02
     */
    @Override
    public void saveStreetScoreRankingToRedis() {
        Integer status = getActiveStatus();

        // 活动未开始不存入缓存
        if (status == ActiveStatusEnum.BEGIN.getValue()) {
            return;
        }

        // 获取街道记录数
        Integer streetCount = teamMapper.selectCount(new LambdaQueryWrapper<Team>()
                .eq(Team::getDeleted, DeletedEnum.NOT_DELETE.getValue())
                .eq(Team::getType, TeamTypeEnum.STREET.getValue())
        );

        if (streetCount == 0) {
            return;
        }

        final int size = 2000;
        int streetPage = (int) Math.ceil((double) streetCount / (double) size);
        LinkedHashMap<String, Object> streetRankIndexMap = new LinkedHashMap<>();

        // 街道
        for (int j = 0; j <= streetPage; j++) {
            int index = j * size;
            // 查询
            List<TeamRankingListVO> streetRankList = teamMapper.selectTeamScoreRanking(index, size, TeamTypeEnum.STREET.getValue());

            for (int j2 = 0; j2 < streetRankList.size(); j2++) {
                TeamRankingListVO streetRankingListVO = streetRankList.get(j2);
                Gson gson = new Gson();
                // 存储
                streetRankIndexMap.put(streetRankingListVO.getId().toString(), gson.toJson(streetRankingListVO));
            }
        }

        redisUtil.hmset(Configs.REDIS_ACTIVE_STREET_RANK, streetRankIndexMap);
    }

    /**
     * 保存街道周榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/30 9:02
     */
    @Override
    public void saveStreetScoreWeekRankingToRedis() {
        // 时间范围
        Integer status = getActiveStatus();

        // 活动未开始不存入缓存
        if (status == ActiveStatusEnum.BEGIN.getValue()) {
            return;
        }

        // 获取街道记录数
        Integer streetCount = teamMapper.selectCount(new LambdaQueryWrapper<Team>()
                .eq(Team::getDeleted, DeletedEnum.NOT_DELETE.getValue())
                .eq(Team::getType, TeamTypeEnum.STREET.getValue())
        );

        if (streetCount == 0) {
            return;
        }

        final int size = 2000;
        int streetPage = (int) Math.ceil((double) streetCount / (double) size);
        LinkedHashMap<String, Object> streetRankIndexMap = new LinkedHashMap<>();

        // 街道
        for (int j = 0; j <= streetPage; j++) {
            int index = j * size;
            // 查询
            List<TeamRankingListVO> streetRankList = teamMapper.selectStreetAndAreaScoreWeekRanking(index, size, TeamTypeEnum.STREET.getValue());

            for (int j2 = 0; j2 < streetRankList.size(); j2++) {
                TeamRankingListVO teamRankingListVO = streetRankList.get(j2);
                Gson gson = new Gson();
                // 存储
                streetRankIndexMap.put(teamRankingListVO.getId().toString(), gson.toJson(teamRankingListVO));
            }
        }

        redisUtil.hmset(Configs.REDIS_ACTIVE_STREET_WEEK_RANK, streetRankIndexMap);
    }

    /**
     * 保存片区总榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/30 9:02
     */
    @Override
    public void saveAreaScoreRankingToRedis() {
        // 活动未开始不存入缓存
        Integer status = getActiveStatus();
        if (status == ActiveStatusEnum.BEGIN.getValue()) {
            return;
        }

        // 获取片区记录数
        Integer areaCount = teamMapper.selectCount(new LambdaQueryWrapper<Team>()
                .eq(Team::getDeleted, DeletedEnum.NOT_DELETE.getValue())
                .eq(Team::getType, TeamTypeEnum.AREA.getValue())
        );

        if (areaCount == 0) {
            return;
        }

        final int size = 2000;
        int areaPage = (int) Math.ceil((double) areaCount / (double) size);
        LinkedHashMap<String, Object> areaRankIndexMap = new LinkedHashMap<>();

        // 片区
        for (int k = 0; k <= areaPage; k++) {
            int index = k * size;
            // 查询
            List<TeamRankingListVO> areaRankList = teamMapper.selectTeamScoreRanking(index, size, TeamTypeEnum.AREA.getValue());

            for (int k2 = 0; k2 < areaRankList.size(); k2++) {
                TeamRankingListVO areaRankingListVO = areaRankList.get(k2);
                Gson gson = new Gson();
                // 存储
                areaRankIndexMap.put(areaRankingListVO.getId().toString(), gson.toJson(areaRankingListVO));
            }
        }

        redisUtil.hmset(Configs.REDIS_ACTIVE_AREA_RANK, areaRankIndexMap);
    }

    /**
     * 保存片区周榜至缓存
     *
     * @param
     * @return void
     * @author qixinyi
     * @date 2021/6/30 9:02
     */
    @Override
    public void saveAreaScoreWeekRankingToRedis() {
        // 时间范围
        Integer status = getActiveStatus();

        // 活动未开始不存入缓存
        if (status == ActiveStatusEnum.BEGIN.getValue()) {
            return;
        }

        // 获取片区记录数
        Integer areaCount = teamMapper.selectCount(new LambdaQueryWrapper<Team>()
                .eq(Team::getDeleted, DeletedEnum.NOT_DELETE.getValue())
                .eq(Team::getType, TeamTypeEnum.AREA.getValue())
        );

        if (areaCount == 0) {
            return;
        }

        final int size = 2000;
        int areaPage = (int) Math.ceil((double) areaCount / (double) size);
        LinkedHashMap<String, Object> areaRankIndexMap = new LinkedHashMap<>();

        // 片区
        for (int k = 0; k <= areaPage; k++) {
            int index = k * size;
            // 查询
            List<TeamRankingListVO> areaRankList = teamMapper.selectStreetAndAreaScoreWeekRanking(index, size, TeamTypeEnum.AREA.getValue());

            for (int k2 = 0; k2 < areaRankList.size(); k2++) {
                TeamRankingListVO teamRankingListVO = areaRankList.get(k2);
                Gson gson = new Gson();
                // 存储
                areaRankIndexMap.put(teamRankingListVO.getId().toString(), gson.toJson(teamRankingListVO));
            }
        }

        redisUtil.hmset(Configs.REDIS_ACTIVE_AREA_WEEK_RANK, areaRankIndexMap);
    }


    /**
     * 根据团队id查找团队
     *
     * @param teamId 团队id
     * @return com.yitu.cpcFounding.api.domain.Team
     * @author qixinyi
     * @date 2021/6/17 9:50
     */
    @Override
    public Team findTeamById(int teamId) {
        return teamMapper.selectById(teamId);
    }

    /**
     * 获取片区街道下级排行榜
     *
     * @param pageIndex   页码
     * @param pageSize    页大小
     * @param parentId    片区/街道id
     * @param rankingType 排行榜类型
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.TeamScoreVO>
     * @author qixinyi
     * @date 2021/7/2 10:50
     */
    @Override
    public JsonResult<TeamScoreVO> findNextLevelScoreSum(int pageIndex, int pageSize, Long parentId,Integer rankingType) {
        // 校验
        if (!EnumUtil.isExist(RankingEnum.class, rankingType)) {
            return JsonResult.fail("排行榜类型状态码数据非法");
        }

        Team team = teamMapper.selectById(parentId);

        if(team!=null){
            int type = team.getType();
            if(type==TeamTypeEnum.TEAM.getValue()){
                return JsonResult.fail("传入非片区/街道的id");
            }
        }else if (team == null) {
            return JsonResult.fail("此片区/街道不存在");
        }

        TeamScoreVO teamScoreVO = new TeamScoreVO();
        List<TeamRankingListVO> results = null;

        // 查询
        if (rankingType == RankingEnum.TOTAL_RANKING.getValue()) {
            results = teamMapper.getNextLevelRankingByParentIdAndType(parentId);
        } else if (rankingType == RankingEnum.WEEK_RANKING.getValue()) {
            results = teamMapper.getNextLevelWeekRankingByParentIdAndType(parentId);
        }

        if (results != null) {
            // 封装列表
            IPage page = PageUtil.getPages(pageIndex, pageSize, results);
            teamScoreVO.setTopList(page);
        }

        return JsonResult.ok(teamScoreVO);
    }

    /**
     * 获取活动状态
     *
     * @param
     * @return java.lang.Integer
     * @author qixinyi
     * @date 2021/6/30 9:13
     */
    private Integer getActiveStatus() {
        JsonResult activeTimeJson = prizeBookService.getActiveTime(ActiveEntranceEnum.USER_SCORE.getValue());
        if (activeTimeJson.getStatus() == ResponseCode.INTERNAL_SERVER_ERROR.getCode()) {
            log.info(activeTimeJson.getMsg());
        }
        ActiveTimeVo activeTimeVo = (ActiveTimeVo) activeTimeJson.getData();

        return activeTimeVo.getStatus();
    }
}

