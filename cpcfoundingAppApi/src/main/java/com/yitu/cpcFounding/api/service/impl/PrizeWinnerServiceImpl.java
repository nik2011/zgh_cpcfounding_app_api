package com.yitu.cpcFounding.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yitu.cpcFounding.api.constant.Configs;
import com.yitu.cpcFounding.api.utils.HttpUtil;
import com.yitu.cpcFounding.api.auth.LoginUserUtil;
import com.yitu.cpcFounding.api.domain.PrizeWinner;
import com.yitu.cpcFounding.api.domain.User;
import com.yitu.cpcFounding.api.enums.DeletedEnum;
import com.yitu.cpcFounding.api.enums.prize.PrizeWinnerEnum;
import com.yitu.cpcFounding.api.mapper.PrizeWinnerMapper;
import com.yitu.cpcFounding.api.mapper.UserMapper;
import com.yitu.cpcFounding.api.redis.RedisUtil;
import com.yitu.cpcFounding.api.service.PrizeWinnerService;
import com.yitu.cpcFounding.api.utils.CommonUtil;
import com.yitu.cpcFounding.api.utils.StringUtil;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.PrizeWinnerVO;
import com.yitu.cpcFounding.api.vo.ShowYearWinnerResponseVo;
import com.yitu.cpcFounding.api.vo.UserVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 获奖用户表服务实现
 *
 * @author luzhichao
 * @date 2021-01-21 11:02:18
 */
@Service
@Log4j2
public class PrizeWinnerServiceImpl implements PrizeWinnerService {

    @Resource
    private PrizeWinnerMapper prizeWinnerMapper;


    @Resource
    private HttpServletRequest request;

    @Resource
    private LoginUserUtil loginUserUtil;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserMapper userMapper;


    /**
     * 获取晒年味获奖用户表列表数据
     *
     * @param pageIndex    页索引
     * @param pageSize     页大小
     * @return 获奖用户表
     * @author luzhichao
     * @date 2021-01-21 11:02:18
     */
    @Override
    public JsonResult<ShowYearWinnerResponseVo> getShowYearWinnerList(int pageIndex, int pageSize) {
        UserVO loginUser = loginUserUtil.getLoginUser();
        // 获取缓存数据
        String item = String.format(Configs.REDIS_SHOW_YEAR_WINNER_KEY, pageIndex, pageSize);
        Object obj = redisUtil.hget(Configs.REDIS_PRIZE_WINNER + PrizeWinnerEnum.YEAR_PRAISE.getValue() , item);
        IPage<PrizeWinnerVO> result;
        if (obj == null) {
            // 查询数据库
            Page<PrizeWinner> page = new Page<>(pageIndex, pageSize);
            LambdaQueryWrapper<PrizeWinner> wrapper = new LambdaQueryWrapper();
            wrapper.eq(PrizeWinner::getDeleted, DeletedEnum.NOT_DELETE.getValue());
            // 获取type=1（晒年味）
            wrapper.eq(PrizeWinner::getType, PrizeWinnerEnum.YEAR_PRAISE.getValue());
            IPage<PrizeWinner> resultPage = prizeWinnerMapper.selectPage(page, wrapper);
            result = new Page<>();
            BeanUtils.copyProperties(resultPage, result);
            if (resultPage.getRecords() != null && resultPage.getRecords().size() != 0) {
                // 查询所有人员信息
                Set<Long> userId = resultPage.getRecords().stream().map(re -> (long)re.getUserId()).collect(Collectors.toSet());
                List<User> users = this.getUserListByIds(userId);
                List<PrizeWinnerVO> collect = resultPage.getRecords().stream().map(re -> {
                    User user = users.stream().filter(ls -> ls.getId() == re.getUserId().intValue()).findAny().orElse(null);
                    if (user == null) {
                        return null;
                    }
                    PrizeWinnerVO prizeWinnerVO = new PrizeWinnerVO();
                    BeanUtils.copyProperties(re, prizeWinnerVO);
                    prizeWinnerVO.setLikeNum(CommonUtil.enlargePraiseNum(re.getLikeNum()));
                    prizeWinnerVO.setUserName(StringUtil.userNameMask(user.getWxUserName()));
                    prizeWinnerVO.setHeadPic(HttpUtil.getNetResourcePath(request, user.getWxHeadPic()));
                    prizeWinnerVO.setWinFlag("Y");
                    return prizeWinnerVO;
                }).filter(z -> z != null).collect(Collectors.toList());
                result.setRecords(collect);
                if (collect != null && collect.size() > 0) {
                    Gson gson = new Gson();
                    // 设置缓存
                    redisUtil.hset(Configs.REDIS_PRIZE_WINNER + PrizeWinnerEnum.YEAR_PRAISE.getValue(), item, gson.toJson(result));
                }
            }
        } else {
            Gson gson = new Gson();
            result = gson.fromJson(obj.toString(), new TypeToken<Page<PrizeWinnerVO>>() {
            }.getType());
        }
        ShowYearWinnerResponseVo showYearWinnerResponseVo = new ShowYearWinnerResponseVo();
        showYearWinnerResponseVo.setTopList(result);
        if (loginUser != null) {
            Object object = redisUtil.get(Configs.REDIS_SELF_POST_RANK + loginUser.getId());
            PrizeWinnerVO prizeWinnerVO = null;
            if (object != null) {
                prizeWinnerVO = JSONObject.toJavaObject(JSONObject.parseObject(object.toString()), PrizeWinnerVO.class);
            }
            if (prizeWinnerVO == null) {
                // 查询数据库
                // 查询我的获奖信息
                LambdaQueryWrapper<PrizeWinner> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(PrizeWinner::getDeleted, DeletedEnum.NOT_DELETE.getValue());
                queryWrapper.eq(PrizeWinner::getType, PrizeWinnerEnum.YEAR_PRAISE.getValue());
                queryWrapper.eq(PrizeWinner::getUserId, loginUser.getId().intValue());
                PrizeWinner prizeWinner = this.prizeWinnerMapper.selectOne(queryWrapper);
                if (prizeWinner != null) {
                    prizeWinnerVO = new PrizeWinnerVO();
                    prizeWinnerVO.setOrderIndex(prizeWinner.getOrderIndex());
                    prizeWinnerVO.setLikeNum(prizeWinner.getLikeNum());
                    prizeWinnerVO.setUserId(loginUser.getId().intValue());
                } else {
                    //prizeWinnerVO = lsShowYearMapper.selectUserRankByUserId(loginUser.getId().intValue());
                }
                if (prizeWinnerVO != null) {
                    prizeWinnerVO.setWinFlag(prizeWinner == null? "N":"Y");
                    // 扩大赞数
                    prizeWinnerVO.setLikeNum(CommonUtil.enlargePraiseNum(prizeWinnerVO.getLikeNum()));
                    //redisUtil.set(Configs.REDIS_SELF_POST_RANK + loginUser.getId(), JSONObject.toJSONString(lsPrizeWinnerVO));
                    redisUtil.set(Configs.REDIS_SELF_POST_RANK + loginUser.getId(), JSONObject.toJSONString(prizeWinnerVO), 120);
                }
            }
            // 避免用户修改头像和昵称
            prizeWinnerVO.setUserName(loginUser.getWxUserName());
            prizeWinnerVO.setHeadPic(loginUser.getWxHeadPic());
            showYearWinnerResponseVo.setSelf(prizeWinnerVO);
        }
        return JsonResult.ok(showYearWinnerResponseVo);
    }

    private List<User> getUserListByIds(Set<Long> userIds){
        if (userIds == null || userIds.size() == 0) {
            return new ArrayList<>();
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getDeleted, DeletedEnum.NOT_DELETE.getValue());
        queryWrapper.in(User::getId, userIds);
        List<User> users = userMapper.selectList(queryWrapper);
        return users;
    }
}
