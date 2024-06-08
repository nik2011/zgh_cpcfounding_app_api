package com.yitu.cpcFounding.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yitu.cpcFounding.api.constant.Configs;
import com.yitu.cpcFounding.api.domain.PrizeBook;
import com.yitu.cpcFounding.api.enums.MemberState;
import com.yitu.cpcFounding.api.mapper.PrizeBookMapper;
import com.yitu.cpcFounding.api.mapper.UserAnswerMapper;
import com.yitu.cpcFounding.api.mapper.UserMapper;
import com.yitu.cpcFounding.api.mapper.UserPrizeMapper;
import com.yitu.cpcFounding.api.redis.RedisUtil;
import com.yitu.cpcFounding.api.service.CommonService;
import com.yitu.cpcFounding.api.utils.DateTimeUtil;
import com.yitu.cpcFounding.api.utils.StringUtil;
import com.yitu.cpcFounding.api.vo.UserVO;
import com.yitu.cpcFounding.api.vo.redis.DynamicUserVO;
import com.yitu.cpcFounding.api.vo.redis.YaoPrizeBookVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 公共服务
 *
 * @author yaoyanhua
 * @date 2020/7/24
 */
@Service
public class CommonServiceImpl implements CommonService {
    private static Logger log = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private PrizeBookMapper prizeBookMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserPrizeMapper userPrizeMapper;
    @Autowired
    private UserAnswerMapper userAnswerMapper;

    /**
     * 获取缓存用户
     *
     * @param sessionId
     * @return com.yitu.hotel.scheduler.domain.RUser
     * @author yaoyanhua
     * @date 2020/12/28 2:02
     */
    public UserVO getLoginUser(String sessionId) {
        Object object = redisUtil.get(Configs.REDIS_LOGIN_USER_PREFIX + sessionId);
        if (object != null) {
            Gson gson = new Gson();
            try {
                UserVO loginUser = gson.fromJson(object.toString(), new TypeToken<UserVO>() {
                }.getType());
                return loginUser;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 更新或插入缓存用户
     *
     * @param sessionId
     * @param loginUser
     * @return boolean
     * @author yaoyanhua
     * @date 2020/12/28 2:11
     */
    public boolean setLoginUser(String sessionId, UserVO loginUser) {
        Gson gson = new Gson();
        redisUtil.set(Configs.REDIS_LOGIN_USER_TOKEN_PREFIX + loginUser.getId(), sessionId, Configs.USER_TOKEN_TIMEOUT_SEC + 5);
        return redisUtil.set(Configs.REDIS_LOGIN_USER_PREFIX + sessionId, gson.toJson(loginUser), Configs.USER_TOKEN_TIMEOUT_SEC);
    }

    /**
     * @param userId
     * @return int
     * @Description: 获取用户摇一摇次数
     * @author liuzhaowei
     * @date 2021/1/22
     */
    public int getUserShakeCount(long userId, String sessionId) {
        Object object = redisUtil.get(Configs.REDIS_USER_SHAKE_COUNT + userId);
        int count = 0;//次数
        if (object == null) {
//            UserVO loginUserVO = getLoginUser(sessionId);
//            if (loginUserVO != null) {
//                if (loginUserVO.getMemberState() == MemberState.Member.getCode()) {
//                    count = 1;
//                }
//            }
            redisUtil.set(Configs.REDIS_USER_SHAKE_COUNT + userId, count, DateTimeUtil.getDiffNextDaySeconds());
        } else {
            count = (int) object;
        }
        return count;
    }

    /**
     * @param userId
     * @return int
     * @Description: 获取用户摇一摇次数 没有初始化
     * @author liuzhaowei
     * @date 2021/1/22
     */
    public Object getUserShakeCount(long userId) {
        return redisUtil.get(Configs.REDIS_USER_SHAKE_COUNT + userId);
    }

    /**
     * @param userId
     * @return int
     * @Description: 增加一次摇一摇机会、增加一次获得摇一摇次数
     * @author liuzhaowei
     * @date 2021/1/22
     */
    public void incrOneUserYaoCount(long userId, String sessionId) {
        getUserShakeCount(userId, sessionId);//初始化
        redisUtil.incr(Configs.REDIS_USER_SHAKE_COUNT + userId, 1);
    }

    /**
     * @param userId
     * @return int
     * @Description: 减少一次摇一摇机会
     * @author liuzhaowei
     * @date 2021/1/22
     */
    public long decrOneUserYaoCount(long userId) {
        return redisUtil.decr(Configs.REDIS_USER_SHAKE_COUNT + userId, 1);
    }

    /**
     * @param
     * @return int
     * @Description: 获取摇一摇奖品列表
     * @author liuzhaowei
     * @date 2021/1/22
     */
    public List<YaoPrizeBookVo> getYaoPrizeList() {
        List<YaoPrizeBookVo> list = new ArrayList<>();
        Object object = redisUtil.get(Configs.REDIS_YAO_PRIZE_LIST);
        if (object != null) {
            Gson gson = new Gson();
            list = gson.fromJson(object.toString(), new TypeToken<List<YaoPrizeBookVo>>() {
            }.getType());
        } else {
            list = prizeBookMapper.getYaoPrizeList();
            Gson gson = new Gson();
            redisUtil.set(Configs.REDIS_YAO_PRIZE_LIST, gson.toJson(list));
        }
        return list;
    }

    /**
     * @param
     * @return int
     * @Description: 设置摇一摇奖品列表
     * @author liuzhaowei
     * @date 2021/1/22
     */
    public boolean setYaoPrizeList() {
        List<YaoPrizeBookVo> list = prizeBookMapper.getYaoPrizeList();
        Gson gson = new Gson();
        return redisUtil.set(Configs.REDIS_YAO_PRIZE_LIST, gson.toJson(list));
    }

    /**
     * @return int
     * @Description: 获取奖品未中奖概率
     * @author liuzhaowei
     * @date 2021/1/22
     */
    @Override
    public List<Integer> getNoWinningProbability() {
        List<Integer> list = new ArrayList<>();
        Object object = redisUtil.get(Configs.REDIS_NO_WINNING_PROBABILITY);
        if (object != null) {
            list = StringUtil.stringToList(object.toString());
        } else {
            PrizeBook prizeBook = prizeBookMapper.getNoWinningProbabilityPrize();
            if (prizeBook != null) {
                list.add(prizeBook.getChanceBeginNum());
                list.add(prizeBook.getChanceEndNum());
                String content = StringUtils.join(list, ",");
                redisUtil.set(Configs.REDIS_NO_WINNING_PROBABILITY, content);
            }
        }
        return list;
    }

    /**
     * @return int
     * @Description: 设置奖品未中奖概率
     * @author liuzhaowei
     * @date 2021/1/22
     */
    @Override
    public boolean setNoWinningProbability() {
        List<Integer> list = new ArrayList<>();
        PrizeBook prizeBook = prizeBookMapper.getNoWinningProbabilityPrize();
        if (prizeBook != null) {
            list.add(prizeBook.getChanceBeginNum());
            list.add(prizeBook.getChanceEndNum());
        } else {
            list.add(0);
            list.add(0);
        }
        String content = StringUtils.join(list, ",");
        return redisUtil.set(Configs.REDIS_NO_WINNING_PROBABILITY, content);
    }

    /**
     * @param prizeId
     * @return int
     * @Description: 获取摇一摇奖品库存
     * @author liuzhaowei
     * @date 2021/1/22
     */
    public int getPrizeStockCount(long prizeId) {
        Object object = redisUtil.get(Configs.REDIS_PRIZE_STOCK_COUNT + prizeId);
        if (object != null) {
            return (int) object;
        }
        return 0;
    }

    /**
     * @param
     * @return int
     * @Description: 设置摇一摇奖品库存
     * @author liuzhaowei
     * @date 2021/1/22
     */
    public boolean setPrizeStockCount() {
        List<YaoPrizeBookVo> list = prizeBookMapper.getYaoPrizeList();
        for (YaoPrizeBookVo item : list) {
            redisUtil.set(Configs.REDIS_PRIZE_STOCK_COUNT + item.getId(), item.getStock());
        }
        return true;
    }

    /**
     * @param prizeId
     * @return int
     * @Description: 摇一摇奖品库存加一
     * @author liuzhaowei
     * @date 2021/1/22
     */
    public long incrPrizeStockCount(long prizeId) {
        return redisUtil.incr(Configs.REDIS_PRIZE_STOCK_COUNT + prizeId, 1);
    }

    /**
     * @param prizeId
     * @return int
     * @Description: 摇一摇奖品库存减一
     * @author liuzhaowei
     * @date 2021/1/22
     */
    public long decrPrizeStockCount(long prizeId) {
        return redisUtil.decr(Configs.REDIS_PRIZE_STOCK_COUNT + prizeId, 1);
    }

    /**
     * @param userId
     * @return int
     * @Description: 获取用户动态信息
     * @author liuzhaowei
     * @date 2021/1/22
     */
    public DynamicUserVO getUserDynamicInfo(long userId, String sessionId) {
        DynamicUserVO userVO = null;
        Object object = redisUtil.get(Configs.REDIS_USER_DYNAMIC_INFO + userId);
        if (object != null) {
            Gson gson = new Gson();
            userVO = gson.fromJson(object.toString(), DynamicUserVO.class);
        } else {
            userVO = new DynamicUserVO();
            // userVO.setYaoCount(1);
            userVO.setGetYaoCount(1);
            userVO.setWhetherAnswer(false);
            UserVO loginUserVO = getLoginUser(sessionId);
            if (loginUserVO != null) {
                if (loginUserVO.getMemberState() == MemberState.Member.getCode()) {
                    userVO.setYaoCount(2);
                }
            }
            setUserDynamicInfo(userId, userVO);
        }
        return userVO;
    }


    /**
     * @param userId
     * @return int
     * @Description: 设置用户动态信息
     * @author liuzhaowei
     * @date 2021/1/22
     */
    public boolean setUserDynamicInfo(long userId, DynamicUserVO userVO) {
        if (userVO != null) {
            Gson gson = new Gson();
            return redisUtil.set(Configs.REDIS_USER_DYNAMIC_INFO + userId, gson.toJson(userVO), DateTimeUtil.getDiffNextDaySeconds());
        }
        return false;
    }

    /**
     * @param userId
     * @return int
     * @Description: 获取用户可以中奖次数
     * @author liuzhaowei
     * @date 2021/4/29
     */
    @Override
    public int getUserPrizeCount(long userId) {
        Object object = redisUtil.get(Configs.REDIS_USER_PRIZE_COUNT + userId);
        int count = 0;//次数
        if (object == null) {
            count = 1;
            long exprieTime = DateTimeUtil.getNextWeekSeconds();
            redisUtil.set(Configs.REDIS_USER_PRIZE_COUNT + userId, count, exprieTime);
        } else {
            count = (int) object;
        }
        return count;
    }

    /**
     * @param userId
     * @return int
     * @Description: 减一次用户中奖次数
     * @author liuzhaowei
     * @date 2021/1/22
     */
    @Override
    public boolean decrOneUserPrizeCount(long userId) {
        return redisUtil.decr(Configs.REDIS_USER_PRIZE_COUNT + userId, 1) > -1;
    }

    /**
     * @param userId
     * @return int
     * @Description: 增加一次用户中奖次数
     * @author liuzhaowei
     * @date 2021/1/22
     */
    @Override
    public boolean incrOneUserPrizeCount(long userId) {
        Object object = redisUtil.get(Configs.REDIS_USER_PRIZE_COUNT + userId);
        if (object != null) {
            return redisUtil.incr(Configs.REDIS_USER_PRIZE_COUNT + userId, 1) > 0;
        }
        return redisUtil.set(Configs.REDIS_USER_PRIZE_COUNT + userId, 1, DateTimeUtil.getDiffNextDaySeconds());
    }

    /**
     * @return java.util.List<java.lang.String>
     * @Description:获取用户答题头像
     * @author liuzhaowei
     * @date 2021/5/17
     */
    @Override
    public List<String> getUserAnswerHeaderList() {
        List<String> resultList = new ArrayList<>();
        Object object = redisUtil.get(Configs.REDIS_USER_ANSWER_HEADER);
        if (object == null) {
            resultList = userAnswerMapper.getUserAnswerHeadPic();
            redisUtil.set(Configs.REDIS_USER_ANSWER_HEADER, JSONObject.toJSONString(resultList), 60 * 60);
        } else {
            resultList = JSONObject.parseArray(object.toString(), String.class);
        }
        return resultList;
    }

    /**
     * 获取用户答题数量
     * @author shenjun
     * @date   2021/7/19 14:57
     * @return
     */
    @Override
    public long getUserAnswerCount() {
        long count  = 0;
        Object object = redisUtil.get(Configs.REDIS_USER_ANSWER_COUNT);
        if (object == null) {
            count = userAnswerMapper.getUserAnswerCount();
            redisUtil.set(Configs.REDIS_USER_ANSWER_COUNT, count, 60 * 60);
        } else {
            count = (int)object;
        }
        return count;
    }

}
