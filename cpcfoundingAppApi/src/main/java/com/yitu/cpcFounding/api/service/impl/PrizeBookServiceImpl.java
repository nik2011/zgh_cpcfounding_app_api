package com.yitu.cpcFounding.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yitu.cpcFounding.api.auth.LoginUserUtil;
import com.yitu.cpcFounding.api.domain.*;
import com.yitu.cpcFounding.api.enums.*;
import com.yitu.cpcFounding.api.enums.prize.PrizeTypeEnum;
import com.yitu.cpcFounding.api.enums.prize.ShakeTakeLogTypeEnum;
import com.yitu.cpcFounding.api.mapper.*;
import com.yitu.cpcFounding.api.rabbitmq.Producer.RabbitmqProducer;
import com.yitu.cpcFounding.api.service.ConfigService;
import com.yitu.cpcFounding.api.service.PrizeBookService;
import com.yitu.cpcFounding.api.service.RevolveTakeLogService;
import com.yitu.cpcFounding.api.service.UserPrizeService;
import com.yitu.cpcFounding.api.utils.CommonUtil;
import com.yitu.cpcFounding.api.utils.HttpUtil;
import com.yitu.cpcFounding.api.utils.StringUtil;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.UserVO;
import com.yitu.cpcFounding.api.vo.prizeBook.ActiveTimeVo;
import com.yitu.cpcFounding.api.vo.prizeBook.LsShakeTakeLogVO;
import com.yitu.cpcFounding.api.vo.prizeBook.PrizeBookVO;
import com.yitu.cpcFounding.api.vo.redis.YaoPrizeBookVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 奖品库表服务实现
 *
 * @author shenjun
 * @date 2021-01-21 11:47:32
 */
@Service
@Slf4j
public class PrizeBookServiceImpl implements PrizeBookService {

    @Autowired
    private PrizeBookMapper prizeBookMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserPrizeService userPrizeService;
    @Autowired
    private RevolveTakeLogService shakeTakeLogService;
    @Autowired
    private UserPrizeMapper userPrizeMapper;
    @Autowired
    private PrizeExchangeCodeMapper prizeExchangeCodeMapper;
    @Autowired
    private LoginUserUtil loginUserUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ConfigService configService;
    @Autowired
    private RabbitmqProducer rabbitmqProducer;
    @Autowired
    private CommonServiceImpl commonService;
    @Autowired
    private RevolveTakeLogMapper shakeTakeLogMapper;
    // private Lock lock = new ReentrantLock();

    /**
     * @return JsonResult
     * @Description: 获取摇一摇数量
     * @author liuzhaowei
     * @date 2021/1/21
     */
    @Override
    public JsonResult getYaoYiYaoCountRedis() {
        int count = 0;
        final UserVO userVO = loginUserUtil.getLoginUser();
        if (userVO == null) {
            return JsonResult.fail("请登录");
        }
        count = commonService.getUserShakeCount(userVO.getId(), userVO.getSessionId());
        count = count <= 0 ? 0 : count;
        return JsonResult.ok(count);
    }

//    /**
//     * @return JsonResult
//     * @Description: 获取摇一摇数量
//     * @author liuzhaowei
//     * @date 2021/1/21
//     */
//    @Override
//    public JsonResult getYaoYiYaoCount() {
//        long userId = loginUserUtil.getUserId();
//        int count = 0;
//        User user = userMapper.selectById(userId);
//        if (user == null) {
//            return JsonResult.fail("用户不存在");
//        }
//        // count = user.getYaoCount();
//        return JsonResult.ok(count);
//    }

    /**
     * @return JsonResult
     * @Description: 执行摇一摇_redis
     * @author liuzhaowei
     * @date 2021/1/21
     */
    @Override
    // @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public JsonResult<YaoPrizeBookVo> doShakeRedis() {
        JsonResult activeTimeJson = getActiveTime(ActiveEntranceEnum.SHAKE.getValue());
        if (activeTimeJson.getStatus() == ResponseCode.INTERNAL_SERVER_ERROR.getCode()) {
            return JsonResult.fail(activeTimeJson.getMsg());
        }
        ActiveTimeVo activeTimeVo = (ActiveTimeVo) activeTimeJson.getData();
        if (activeTimeVo.getStatus() == 0) {
            return JsonResult.fail("活动未开始");
        } else if (activeTimeVo.getStatus() == 2) {
            return JsonResult.fail("活动已结束");
        }
        final UserVO userVO = loginUserUtil.getLoginUser();
        if (userVO == null) {
            return JsonResult.fail("请登录");
        }
        long userId = userVO.getId();
        String userName = userVO.getWxUserName();
        String userWinningPrizeId = userVO.getUserWinningPrizeId();

        Object countObject = commonService.getUserShakeCount(userId);
        if (countObject == null) {
            return JsonResult.build(ResponseCode.INTERNAL_SERVER_ERROR, "系统繁忙，稍后再试!");
        }
        int userShakeCount = commonService.getUserShakeCount(userId, userVO.getSessionId());
        int userType = userVO.getMemberState().equals(MemberState.Member.getCode()) ? 2 : 1;//会员状态
        //验证次数
        if (userShakeCount <= 0) {
            return JsonResult.fail("您没有抽奖次数");
        }
        long result = 0;
        //用户摇一摇次数减一
        result = commonService.decrOneUserYaoCount(userId);
        if (result < 0) {
            return JsonResult.fail("您没有抽奖次数!");
        }
        if (userVO.getMemberState().equals(MemberState.Common.getCode())) {
            return JsonResult.fail("非会员不能参加抽奖，请先认证");
        }
        //未中奖返回对象
        YaoPrizeBookVo noYaoPrizeBookVo = CommonUtil.getNoWinningPrize(request);
        int randomNum = RandomUtils.nextInt(1, 10001);
        Gson gson = new Gson();
        RevolveTakeLog takeLog = shakeTakeLogService.packageEntity(ShakeTakeLogTypeEnum.SHAKE, -1, 0, userId, userName);
        //发消息队列Vo
        LsShakeTakeLogVO lsShakeTakeLogVO = new LsShakeTakeLogVO();
        BeanUtils.copyProperties(takeLog, lsShakeTakeLogVO);
        lsShakeTakeLogVO.setPrizeValue(noYaoPrizeBookVo.getPrizeValue());
        String takeLogStr = gson.toJson(lsShakeTakeLogVO);

        //获取用户当天可以中奖次数
        long userPrizeCount = commonService.getUserPrizeCount(userId);
        if (userPrizeCount <= 0) {
            rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
            return JsonResult.ok(noYaoPrizeBookVo);
        }

        //奖品未中奖概率
        List<Integer> noWinningProbability = commonService.getNoWinningProbability();
        if (noWinningProbability.size() == 2) {
            if (randomNum >= noWinningProbability.get(0) && randomNum <= noWinningProbability.get(1)) {
                rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
                return JsonResult.ok(noYaoPrizeBookVo);
            }
        }
        List<YaoPrizeBookVo> yaoPrizeBookVoList = commonService.getYaoPrizeList();
        if (yaoPrizeBookVoList.size() == 0) {
            log.error("摇一摇未设置奖品");
        }

        List<YaoPrizeBookVo> selectPrizeList = yaoPrizeBookVoList.stream().filter(k -> randomNum >= k.getChanceBeginNum()
                && randomNum <= k.getChanceEndNum()).collect(Collectors.toList());
        if (selectPrizeList.size() == 0) {
            rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
            return JsonResult.ok(noYaoPrizeBookVo);
        }
        YaoPrizeBookVo yaoPrizeBookVo = selectPrizeList.get(0);

        long prizeId = yaoPrizeBookVo.getId();
        //判断库存
        long stock = commonService.getPrizeStockCount(prizeId);
        if (stock <= 0) {
            rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
            return JsonResult.ok(noYaoPrizeBookVo);
        }
        //什么都没有
        if (yaoPrizeBookVo.getPrizeType() == PrizeTypeEnum.NOTHING.getCode()) {
            rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
            return JsonResult.ok(noYaoPrizeBookVo);
        }
        //判断类型
        if ((userType & yaoPrizeBookVo.getMemberType()) == 0) {
            rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
            return JsonResult.ok(noYaoPrizeBookVo);
        }

        //判断是否中过奖
        List<Long> userWinningPrizeIds = StringUtil.stringToLongList(userWinningPrizeId);
        if (userWinningPrizeIds.size() > 0 && userWinningPrizeIds.contains(prizeId)) {
            rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
            return JsonResult.ok(noYaoPrizeBookVo);
        }

        //减库存
        result = commonService.decrPrizeStockCount(prizeId);
        if (result < 0) {
            rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
            return JsonResult.ok(noYaoPrizeBookVo);
        }
        //不是次数增加
        if (yaoPrizeBookVo.getPrizeType() != PrizeTypeEnum.ADD_COUNT.getCode()) {
            boolean redisResult = false;
            //减一次用户中奖次数
            if (!commonService.decrOneUserPrizeCount(userId)) {
                commonService.incrPrizeStockCount(prizeId);
                rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
                return JsonResult.ok(noYaoPrizeBookVo);
            }
            userWinningPrizeIds.add(prizeId);
            userVO.setUserWinningPrizeId(StringUtils.join(userWinningPrizeIds, ","));
            //更新或插入缓存用户
            redisResult = commonService.setLoginUser(userVO.getSessionId(), userVO);
            if (!redisResult) {
                commonService.incrPrizeStockCount(prizeId);
                rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
                return JsonResult.ok(noYaoPrizeBookVo);
            }
        }

        //次数增加
        if (yaoPrizeBookVo.getPrizeType() == PrizeTypeEnum.ADD_COUNT.getCode()) {
            lsShakeTakeLogVO.setInOrOut(0);
            commonService.incrOneUserYaoCount(userId, userVO.getSessionId());
        }

        lsShakeTakeLogVO.setPrizeId(prizeId);
        lsShakeTakeLogVO.setPrizeType(yaoPrizeBookVo.getPrizeType());
        String lsShakeTakeLogVOStr = gson.toJson(lsShakeTakeLogVO);
        rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_WINNING_QUEUE, lsShakeTakeLogVOStr);

        yaoPrizeBookVo.setImageUrl(HttpUtil.getNetResourcePath(request, yaoPrizeBookVo.getImageUrl()));
        return JsonResult.ok(yaoPrizeBookVo);
    }

//    /**
//     * @return JsonResult
//     * @Description: 执行摇一摇
//     * @author liuzhaowei
//     * @date 2021/1/21
//     */
//    @Override
//    public JsonResult<YaoPrizeBookVo> doShake() {
//        JsonResult activeTimeJson = getActiveTime(ActiveEntranceEnum.SHAKE.getValue());
//        if (activeTimeJson.getStatus() == ResponseCode.INTERNAL_SERVER_ERROR.getCode()) {
//            return JsonResult.fail(activeTimeJson.getMsg());
//        }
//        ActiveTimeVo activeTimeVo = (ActiveTimeVo) activeTimeJson.getData();
//        if (activeTimeVo.getStatus() == 0) {
//            return JsonResult.fail("活动未开始");
//        } else if (activeTimeVo.getStatus() == 2) {
//            return JsonResult.fail("活动已结束");
//        }
//        final UserVO userVO = loginUserUtil.getLoginUser();
//        if (userVO == null) {
//            return JsonResult.fail("请登录");
//        }
//        long userId = userVO.getId();
//        String userName = userVO.getWxUserName();
////        long userId = 5;
////        String userName = "nik";
//        User user = userMapper.selectById(userId);
//        if (user == null) {
//            return JsonResult.fail("用户不存在");
//        }
//        int userType = user.getMemberState() > 0 ? 2 : 1;//会员状态
//        //验证次数
////        if (user.getYaoCount() <= 0) {
////            return JsonResult.fail("您没有抽奖次数");
////        }
//        //用户摇一摇次数减一
//        int result = 0;
////        result = lsUserMapper.minusYaoCount(userId);
////        if (result == 0) {
////            throw new RuntimeException("系统繁忙，请稍后再试!");
////        }
//        //未中奖返回对象
//        YaoPrizeBookVo noYaoPrizeBookVo = CommonUtil.getNoWinningPrize(request);
//        int randomNum = RandomUtils.nextInt(1, 10001);
//        Gson gson = new Gson();
//        RevolveTakeLog takeLog = shakeTakeLogService.packageEntity(ShakeTakeLogTypeEnum.SHAKE, -1, 0, userId, userName);
//        String takeLogStr = gson.toJson(takeLog);
//
//        //判断用户今天中奖次数
//        int nowPrizeCount = userPrizeMapper.getTodayUserPrizeCountByPrizeId(userId);
//        if (nowPrizeCount >= 2) {
//            // lsShakeTakeLogService.insertRecord(ShakeTakeLogTypeEnum.SHAKE, -1, 0, userId, userName);
//            rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
//            return JsonResult.ok(noYaoPrizeBookVo);
//        }
//        YaoPrizeBookVo yaoPrizeBookVo = prizeBookMapper.getPrizeByProbability(randomNum);
//        if (yaoPrizeBookVo == null) {
//            // lsShakeTakeLogService.insertRecord(ShakeTakeLogTypeEnum.SHAKE, -1, 0, userId, userName);
//            rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
//            return JsonResult.ok(noYaoPrizeBookVo);
//        }
//        if (yaoPrizeBookVo.getStock() <= 0) {
//            rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
//            return JsonResult.ok(noYaoPrizeBookVo);
//        }
//        //什么都没有
//        if (yaoPrizeBookVo.getPrizeType() == PrizeTypeEnum.NOTHING.getCode()) {
//            rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
//            return JsonResult.ok(noYaoPrizeBookVo);
//        }
//        //判断类型
//        if ((userType & yaoPrizeBookVo.getMemberType()) == 0) {
//            rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
//            return JsonResult.ok(noYaoPrizeBookVo);
//        }
//        //次数增加
//        if (yaoPrizeBookVo.getPrizeType() == PrizeTypeEnum.ADD_COUNT.getCode()) {
////            result = lsUserMapper.addYaoCount(userId);
////            if (result == 0) {
////                return JsonResult.ok(noYaoPrizeBookVo);
////            }
//            takeLog.setInOrOut(1);
//            takeLogStr = gson.toJson(takeLog);
//            rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
//            return JsonResult.ok(yaoPrizeBookVo);
//        }
//        long prizeId = yaoPrizeBookVo.getId();
//
//        //判断是否中过奖
//        int prizeCount = userPrizeMapper.getUserPrizeCountByPrizeId(prizeId, userId);
//        if (prizeCount > 0) {
//            // lsShakeTakeLogService.insertRecord(ShakeTakeLogTypeEnum.SHAKE, -1, 0, userId, userName);
//            rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
//            return JsonResult.ok(noYaoPrizeBookVo);
//        }
//
//        //减库存
//        result = prizeBookMapper.minusPrizeStock(prizeId);
//        if (result == 0) {
//            // lsShakeTakeLogService.insertRecord(ShakeTakeLogTypeEnum.SHAKE, -1, 0, userId, userName);
//            rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
//            return JsonResult.ok(noYaoPrizeBookVo);
//        }
//        String exchangeCode = "";
//        if (yaoPrizeBookVo.getPrizeType() == PrizeTypeEnum.VIRTUAL.getCode()) {
//            PrizeExchangeCode exchangeCodeEntity = prizeExchangeCodeMapper.getActiveExchangeCode(prizeId);
//            if (exchangeCodeEntity == null) {
//                throw new RuntimeException("系统繁忙，请稍后再试!");
//            }
//            result = prizeExchangeCodeMapper.updateExchangeCodeStatus(exchangeCodeEntity.getId());
//            if (result == 0) {
//                throw new RuntimeException("系统繁忙，请稍后再试!");
//            }
//            exchangeCode = exchangeCodeEntity.getExchangeCode();
//        }
//        String ip = HttpUtil.getIPAddress(request);
//        long userPrizeId = userPrizeService.insertRecord(prizeId, userId, userName, exchangeCode, ip);
//        if (userPrizeId == 0) {
//            throw new RuntimeException("系统繁忙，请稍后再试!");
//        }
////        result = lsShakeTakeLogService.insertRecord(ShakeTakeLogTypeEnum.SHAKE, -1, userPrizeId, userId, userName);
////        if (result == 0) {
////            throw new RuntimeException("系统繁忙，请稍后再试!");
////        }
//        takeLog.setUserPrizeId((int) userPrizeId);
//        takeLogStr = gson.toJson(takeLog);
//        rabbitmqProducer.sendMessage(QueueEnum.YEAR_SHAKE_LOG_QUEUE, takeLogStr);
//
//        yaoPrizeBookVo.setImageUrl(HttpUtil.getNetResourcePath(request, yaoPrizeBookVo.getImageUrl()));
//        return JsonResult.ok(yaoPrizeBookVo);
//    }

//    /**
//     * @param data
//     * @return void
//     * @Description: 处理摇一摇中奖记录队列
//     * @author liuzhaowei
//     * @date 2021/1/28
//     */
//    //@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
//    public void handelShakeWinningQueue(String data) {
//        Gson gson = new Gson();
//        LsShakeTakeLogVO lsShakeTakeLogVO = gson.fromJson(data, LsShakeTakeLogVO.class);
//        long prizeId = lsShakeTakeLogVO.getPrizeId();
//        //减库存
//        long result = prizeBookMapper.minusPrizeStock(prizeId);
//        if (result == 0) {
//            log.error("rabbit接收year_shake_winning_queue信息：减库存失败 {}", data);
//            return;
//            //throw new RuntimeException("减库存失败");
//        }
//
//        String exchangeCode = "";
//        if (lsShakeTakeLogVO.getPrizeType() == PrizeTypeEnum.VIRTUAL.getCode()) {
//            PrizeExchangeCode exchangeCodeEntity = prizeExchangeCodeMapper.getActiveExchangeCode(prizeId);
//            if (exchangeCodeEntity == null) {
//                log.error("rabbit接收year_shake_winning_queue信息：获取兑换码为null{}", data);
//                return;
//                // throw new RuntimeException("获取兑换码为null");
//            } else {
//                result = prizeExchangeCodeMapper.updateExchangeCodeStatus(exchangeCodeEntity.getId());
//                if (result == 0) {
//                    log.error("rabbit接收year_shake_winning_queue信息：兑换码修改状态失败{}", data);
//                    // throw new RuntimeException("兑换码修改状态失败");
//                    return;
//                }
//                exchangeCode = exchangeCodeEntity.getExchangeCode();
//            }
//        }
//        long userPrizeId = userPrizeService.insertRecord(prizeId, lsShakeTakeLogVO.getUserId(),
//                lsShakeTakeLogVO.getUserName(), exchangeCode, lsShakeTakeLogVO.getIp());
//        if (userPrizeId == 0) {
//            log.error("rabbit接收year_shake_winning_queue信息：添加用户奖品记录失败{}", data);
//            return;
//            //throw new RuntimeException("添加用户奖品记录失败");
//        }
//        RevolveTakeLog takeLog = new RevolveTakeLog();
//        BeanUtils.copyProperties(lsShakeTakeLogVO, takeLog);
//        takeLog.setUserPrizeId((int) userPrizeId);
//        shakeTakeLogMapper.insert(takeLog);
////        if (lsShakeTakeLogVO.getPrizeType() != PrizeTypeEnum.ADD_COUNT.getCode()) {
////            //减摇一摇次数
////            lsUserMapper.minusYaoCount(takeLog.getUserId());
////        }
//        return;
//    }

    /**
     * 根据活动类型查询奖品列表
     *
     * @param activeType 活动类型
     * @return JsonResult<java.util.List < PrizeBookVO>>
     * @author qinmingtong
     * @date 2021/1/23 8:52
     */
    @Override
    public JsonResult<List<PrizeBookVO>> prizeBookList(Integer activeType) {
        //条件查询
        LambdaQueryWrapper<PrizeBook> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(PrizeBook::getTitle, PrizeBook::getImageUrl, PrizeBook::getMemberType,
                PrizeBook::getActiveType, PrizeBook::getRemark, PrizeBook::getExplainContent);
        queryWrapper.eq(PrizeBook::getIsShelves, YesOrNoEnum.YES.getValue());
        queryWrapper.eq(activeType != null, PrizeBook::getActiveType, activeType);
        queryWrapper.notIn(PrizeBook::getPrizeType, PrizeTypeEnum.ADD_COUNT.getCode(),
                PrizeTypeEnum.NOTHING.getCode());
        queryWrapper.eq(PrizeBook::getDeleted, DeletedEnum.NOT_DELETE.getValue());
        List<PrizeBook> data = prizeBookMapper.selectList(queryWrapper);
        //转换成VO
        List<PrizeBookVO> resultData = data.stream().map(o -> {
            PrizeBookVO prizeBookVO = new PrizeBookVO();
            BeanUtils.copyProperties(o, prizeBookVO);
            prizeBookVO.setImageUrl(HttpUtil.getNetResourcePath(request, prizeBookVO.getImageUrl()));
            return prizeBookVO;
        }).collect(Collectors.toList());
        return JsonResult.ok(resultData);
    }

    /**
     * @param activeType
     * @return JsonResult<ActiveTimeVo>
     * @Description: 获取活动开始结束时间
     * @author liuzhaowei
     * @date 2021/1/23
     */
    @Override
    public JsonResult<ActiveTimeVo> getActiveTime(int activeType) {
        List<Config> configList = configService.getConfigListByType(ConfigEnum.ACTIVE_ENTRANCE.getCode());
        if (configList.size() == 0) {
            return JsonResult.fail("请配置活动开始结束时间");
        }
        ActiveEntranceEnum entranceEnum = ActiveEntranceEnum.parse(activeType);
        if (entranceEnum == null) {
            return JsonResult.fail("活动类型有误");
        }
        ActiveTimeVo activeTimeVo = new ActiveTimeVo();
        String keyId = entranceEnum.getValue().toString();
        List<Config> selectList = configList.stream().filter(k -> k.getKeyId().equals(keyId)).collect(Collectors.toList());
        if (selectList.size() == 0) {
            return JsonResult.fail("请配置该活动开始结束时间");
        }
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        activeTimeVo = gson.fromJson(selectList.get(0).getKeyValue(), activeTimeVo.getClass());
        if (activeTimeVo == null) {
            return JsonResult.fail("该活动开始结束时间配置有误");
        }
        Date now = new Date();
        activeTimeVo.setStatus(1);
        if (now.getTime() < activeTimeVo.getBeginDate().getTime()) {
            activeTimeVo.setStatus(0);
        } else if (now.getTime() > activeTimeVo.getEndDate().getTime()) {
            activeTimeVo.setStatus(2);
        }
        return JsonResult.ok(activeTimeVo);
    }

}
