package com.yitu.cpcFounding.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yitu.cpcFounding.api.auth.LoginUserUtil;
import com.yitu.cpcFounding.api.constant.Configs;
import com.yitu.cpcFounding.api.domain.Config;
import com.yitu.cpcFounding.api.domain.DangHistory;
import com.yitu.cpcFounding.api.domain.User;
import com.yitu.cpcFounding.api.enums.*;
import com.yitu.cpcFounding.api.mapper.DangHistoryMapper;
import com.yitu.cpcFounding.api.mapper.UserMapper;
import com.yitu.cpcFounding.api.rabbitmq.Producer.RabbitmqProducer;
import com.yitu.cpcFounding.api.redis.RedisUtil;
import com.yitu.cpcFounding.api.service.ConfigService;
import com.yitu.cpcFounding.api.service.DangHistoryService;
import com.yitu.cpcFounding.api.service.PrizeBookService;
import com.yitu.cpcFounding.api.service.UserScoreService;
import com.yitu.cpcFounding.api.utils.BeanCopyUtil;
import com.yitu.cpcFounding.api.utils.DateTimeUtil;
import com.yitu.cpcFounding.api.utils.HttpUtil;
import com.yitu.cpcFounding.api.utils.StringUtil;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.UserVO;
import com.yitu.cpcFounding.api.vo.dangHistory.DangHistoryDetailVO;
import com.yitu.cpcFounding.api.vo.dangHistory.DangHistoryLabelVO;
import com.yitu.cpcFounding.api.vo.dangHistory.DangHistoryListVO;
import com.yitu.cpcFounding.api.vo.prizeBook.ActiveTimeVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 *党史小程序服务实现
 * File Name: DangHistoryServiceImpl
 * author: wangping
 * Date: 2021/6/7 15:58
 */
@Service
@Slf4j
public class DangHistoryServiceImpl implements DangHistoryService {

    @Resource
    private DangHistoryMapper dangHistoryMapper;

    @Autowired
    private RabbitmqProducer rabbitmqProducer;

    @Resource
    private LoginUserUtil loginUserUtil;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ConfigService configService;

    @Resource
    UserScoreService userScoreService;

    @Autowired
    private PrizeBookService prizeBookService;


    private Lock lock = new ReentrantLock();

    /**
     * 党史列表查询
     *
     * @param lableId   标签分类 默认0查询推荐
     * @param title     标题
     * @param pageIndex 页码
     * @param pageSize  页大小
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.dangHistory.DangHistoryListVO>
     * @author wangping
     * @date 2021/6/7 16:14
     */
    @Override
    public JsonResult<IPage<DangHistoryListVO>> getDangHisList(String lableId, String title, int pageIndex, int pageSize) {
        //党史列表信息
        Page<DangHistory> page = new Page<>(pageIndex, pageSize);
        LambdaQueryWrapper<DangHistory> wrapper = new LambdaQueryWrapper();
        wrapper.select(DangHistory::getId,DangHistory::getTitle,DangHistory::getType,DangHistory::getCoverType,
                DangHistory::getUrl,DangHistory::getTop,DangHistory::getAddDate);//查询部分字段
        wrapper.eq(DangHistory::getDeleted, DeletedEnum.NOT_DELETE.getValue());

        if(!StringUtil.isEmpty(lableId) && !"-1".equals(lableId)){
            if(lableId.equals("0")){//查询推荐的数据
                wrapper.eq(DangHistory::getRecommend,"1");
            }else{
                wrapper.eq(DangHistory::getLableId,lableId);
            }
        }

        if(!StringUtil.isEmpty(title)){
            wrapper.like(DangHistory::getTitle,title);
        }
        wrapper.orderByDesc(DangHistory::getTop);
        wrapper.orderByDesc(DangHistory::getSort);
        wrapper.orderByDesc(DangHistory::getAddDate);
        UUID uuid = UUID.randomUUID();
        log.error("党史列表（分页）sql-开始执行("+uuid+"):"+ DateTimeUtil.formatCurrent(""));
        IPage<DangHistory> resultPage = dangHistoryMapper.selectPage(page, wrapper);
        log.error("党史列表（分页）sql-结束执行("+uuid+"):"+ DateTimeUtil.formatCurrent(""));
        //数据对象转换
        IPage<DangHistoryListVO> resultPageList=resultPage.convert(o -> {
                DangHistoryListVO  dangHistoryVO =BeanCopyUtil.of(o, new DangHistoryListVO()).copy(BeanUtils::copyProperties).get();
        //图片地址转换
        List<String> list=new ArrayList<>();
        if(!StringUtils.isEmpty(o.getUrl())){
            for(String url:o.getUrl().split(",")){
                list.add(HttpUtil.getWebResourcePath(url));
            }
            dangHistoryVO.setUrl(list);
        }
        return dangHistoryVO;
        });

        return JsonResult.ok(resultPageList);

    }

    /**
     * 获取党史详情信息
     *
     * @param id
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.dangHistory.DangHistoryDetailVO>
     * @author wangping
     * @date 2021/6/7 16:43
     */
    @Override
    public JsonResult<DangHistoryDetailVO> getDangHisDetail(Integer id) {
        if(id == null){
            return JsonResult.fail("参数不能为空");
        }
        LambdaQueryWrapper<DangHistory> wrapper = new LambdaQueryWrapper();
        wrapper.eq(DangHistory::getDeleted, DeletedEnum.NOT_DELETE.getValue());
        wrapper.eq(DangHistory::getId, id);
        DangHistory dangHistory= dangHistoryMapper.selectOne(wrapper);
        if(dangHistory == null){
            return JsonResult.fail("数据不存在");
        }
        DangHistoryDetailVO dangHistoryDetailVO= BeanCopyUtil.of(dangHistory,new DangHistoryDetailVO()).copy(BeanUtils::copyProperties).get();
        return JsonResult.ok(dangHistoryDetailVO);
    }

    /**
     * 添加党史阅读量
     *
     * @param id
     * @return com.yitu.cpcFounding.api.vo.JsonResult
     * @author wangping
     * @date 2021/6/7 17:28
     */
    @Override
    public JsonResult addVisitNum(Integer id) {
        if(id == null){
            return JsonResult.fail("参数不能为空");
        }

        //加入消息队列，添加阅读量
        DangHistory dangHistory= dangHistoryMapper.selectById(id);
        if(dangHistory != null){
            Gson gson = new Gson();
            String dangHisStr = gson.toJson(dangHistory);
            rabbitmqProducer.sendMessage(QueueEnum.VISIT_NUM_QUEUE,dangHisStr);
            return JsonResult.ok();
        }else{
            return JsonResult.fail("未查到数据");
        }

    }

    /**
     * 阅读文章添加积分
     *
     * 逻辑：将当前用户阅读文章加积分次数和文章id存入两个缓存
     * 通过缓存次数和文章是否已阅读，判断每次调用是否要加积分
     *
     * @param id 当前文章id
     * @return com.yitu.cpcFounding.api.vo.JsonResult
     * @author wangping
     * @date 2021/6/8 9:25
     */
    @Override
    public JsonResult readAddScore(Integer id) {

        LambdaQueryWrapper<DangHistory> wrapper = new LambdaQueryWrapper();
        wrapper.eq(DangHistory::getDeleted, DeletedEnum.NOT_DELETE.getValue());
        wrapper.eq(DangHistory::getId, id);
        DangHistory dangHistory= dangHistoryMapper.selectOne(wrapper);
        if(dangHistory == null){
            return JsonResult.fail("文章不存在");
        }

        Map map=new HashMap();
        map.put("scoreStatus",0);

        //获取当前登录用户
        UserVO loginUser = loginUserUtil.getLoginUser();
        User user = userMapper.selectById(loginUser.getId());
        if (StringUtils.isEmpty(user)) {
            return JsonResult.fail("用户不存在");
        }

        //获取实名用户
        if (MemberState.Member.getCode() != user.getMemberState()) {
            return JsonResult.fail("非实名会员，请认证");
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
            return JsonResult.ok(map);
        }

        //上锁
        lock.lock();
        try {
            //redisUtil.del(Configs.REDIS_READ_ARTIClE_SCORE_COUNT);
            //阅读文章积分次数
            Object obj = redisUtil.hget(Configs.REDIS_READ_ARTIClE_SCORE_COUNT+DateTimeUtil.formatCurrent(DateTimeUtil.YYYYMMDD),
                    loginUser.getId().toString());
            if(obj==null) {
                //用户阅读文章加积分次数加入缓存
                redisUtil.hset(Configs.REDIS_READ_ARTIClE_SCORE_COUNT+DateTimeUtil.formatCurrent(DateTimeUtil.YYYYMMDD),
                        loginUser.getId().toString(), 1, DateTimeUtil.getDiffNextDaySeconds());
                //用户阅读的文章加入缓存，用于判断是否阅读相同文章
                Gson gson = new Gson();
                List<Long> dangList=new ArrayList<>();
                dangList.add(dangHistory.getId());
                redisUtil.hset(Configs.REDIS_READ_ARTIClE_COUNT+DateTimeUtil.formatCurrent(DateTimeUtil.YYYYMMDD),
                        loginUser.getId().toString(),gson.toJson(dangList),DateTimeUtil.getDiffNextDaySeconds());
                //medalNumAdVO.setSurplusNumber(2);

            }else{
                if((int)obj >= 10){
                    //返回数据
                    //medalNumAdVO.setSurplusNumber(-1);
                    return JsonResult.ok(map);
                }else{
                    //先获取用户当日阅读的文章
                    Object articleObj = redisUtil.hget(Configs.REDIS_READ_ARTIClE_COUNT+DateTimeUtil.formatCurrent(DateTimeUtil.YYYYMMDD),
                            loginUser.getId().toString());
                    Gson gson = new Gson();
                    List<Long> listId = gson.fromJson(articleObj.toString(), new TypeToken< List<Long>>() {
                    }.getType());

                    for(Long dangId:listId){
                        if(dangHistory.getId().equals(dangId)){
                            //如果存在直接return返回
                            return JsonResult.ok(map);
                        }
                    }
                    //不存在则加入积分
                    listId.add(dangHistory.getId());

                    //更新用户阅读文章
                    redisUtil.hset(Configs.REDIS_READ_ARTIClE_COUNT+DateTimeUtil.formatCurrent(DateTimeUtil.YYYYMMDD),
                            loginUser.getId().toString(),gson.toJson(listId));
                    //加积分次数完成一次加一
                    redisUtil.hincr(Configs.REDIS_READ_ARTIClE_SCORE_COUNT+DateTimeUtil.formatCurrent(DateTimeUtil.YYYYMMDD),
                            loginUser.getId().toString(),1);
                }
            }
        }finally{
        //释放锁
        lock.unlock();
    }

        //加入积分逻辑
        JsonResult addScoreJson= userScoreService.addScore(SourceTypeEnum.READ_ARTICLE.getValue(),dangHistory.getId(),null);

        if (addScoreJson.getStatus() == ResponseCode.INTERNAL_SERVER_ERROR.getCode()) {
            return JsonResult.fail(addScoreJson.getMsg());
        }
        map.put("scoreStatus",1);
        map.put("scoreStr","积分：+5");
        return JsonResult.ok(map);
    }

    /**
     * 获取党史标签
     *
     * @return JsonResult
     * @author wangping
     * @date 2021/6/4 15:53
     */
    @Override
    public JsonResult<List<DangHistoryLabelVO>> getDangHistoryLabel() {

        List<Config> configs = configService.getConfigListByType(ConfigEnum.DANG_HIS_LABEL.getCode());
        List<DangHistoryLabelVO> dangHistoryLabelVOS=configs.stream().map(o ->{
            DangHistoryLabelVO dangHistoryLabelVO=new DangHistoryLabelVO();
            dangHistoryLabelVO.setLableId(o.getKeyId());
            dangHistoryLabelVO.setLableValue(o.getKeyValue());
            return dangHistoryLabelVO;
        }).collect(Collectors.toList());
        return JsonResult.ok(dangHistoryLabelVOS);
    }



}

