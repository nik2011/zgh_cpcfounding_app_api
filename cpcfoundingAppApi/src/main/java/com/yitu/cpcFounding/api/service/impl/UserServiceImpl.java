package com.yitu.cpcFounding.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.yitu.cpcFounding.api.auth.LoginUserUtil;
import com.yitu.cpcFounding.api.constant.Configs;
import com.yitu.cpcFounding.api.domain.User;
import com.yitu.cpcFounding.api.enums.*;
import com.yitu.cpcFounding.api.mapper.TeamMapper;
import com.yitu.cpcFounding.api.mapper.UserMapper;
import com.yitu.cpcFounding.api.mapper.UserPrizeMapper;
import com.yitu.cpcFounding.api.mapper.UserScoreMapper;
import com.yitu.cpcFounding.api.po.DatePo;
import com.yitu.cpcFounding.api.redis.RedisUtil;
import com.yitu.cpcFounding.api.service.PrizeBookService;
import com.yitu.cpcFounding.api.service.TradeUnionService;
import com.yitu.cpcFounding.api.service.UserService;
import com.yitu.cpcFounding.api.utils.HttpUtil;
import com.yitu.cpcFounding.api.utils.TencentUtil;
import com.yitu.cpcFounding.api.utils.WeekRankingUtil;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.TeamScoreVO;
import com.yitu.cpcFounding.api.vo.UserDetailVO;
import com.yitu.cpcFounding.api.vo.UserVO;
import com.yitu.cpcFounding.api.service.CommonService;
import com.yitu.cpcFounding.api.vo.prizeBook.ActiveTimeVo;
import com.yitu.cpcFounding.api.vo.team.TeamRankingListVO;
import com.yitu.cpcFounding.api.vo.userScore.UserRankingListVO;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 用户表服务
 *
 * @author yaoyanhua
 * @date 2021-01-21 11:47:39
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Value("${wechat.appId}")
    private String appId;
    @Value("${wechat.appSecret}")
    private String appSecret;
    @Value("${wechat.authCode2SessionUrl}")
    private String authCode2SessionUrl;
    @Value("${wechat.defaultUserHeadImg}")
    private String defaultUserHeadImg;
    @Autowired
    private UserPrizeMapper userPrizeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private CommonService commonService;
    @Resource
    LoginUserUtil loginUserUtil;
    @Autowired
    private TradeUnionService tradeUnionService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private WeekRankingUtil weekRankingUtil;
    @Autowired
    private PrizeBookService prizeBookService;
    @Autowired
    private UserScoreMapper userScoreMapper;
    @Autowired
    private TeamMapper teamMapper;

    /**
     * 微信登录
     *
     * @param code
     * @param avatarUrl
     * @param nickName
     * @param req
     * @return JsonResult<com.yitu.women.api.dto.LoginUser>
     * @author yaoyanhua
     * @date 2021/1/21 21:53
     */
    @Override
    public JsonResult<UserVO> loginByWeChat(String code, String avatarUrl, String nickName,
                                            HttpServletRequest req) throws IOException {
        final String _appId = URLEncoder.encode(appId, "UTF-8");
        final String _appSecret = URLEncoder.encode(appSecret, "UTF-8");
        Map<String, String> params = new HashMap<>();
        params.put("appid", _appId);
        params.put("secret", _appSecret);
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        Response res = HttpUtil.postRequest(authCode2SessionUrl, HttpUtil.getBody(params));
        if (res != null && res.isSuccessful() && res.body() != null) {
            String result = res.body().string();
            if (StringUtils.isBlank(result)) {
                return JsonResult.fail("解析失败");
            }

            JSONObject obj = JSONObject.parseObject(result);
            final String errCode = obj.getString("errcode");
            if (!StringUtils.isBlank(errCode)) {
                // code 错误
                return JsonResult.fail(obj.getString("errmsg"));
            }

            final String openId = obj.getString("openid");
            if (StringUtils.isBlank(openId)) {
                return JsonResult.fail("获取信息失败");
            }

//            if(StringUtils.isNotBlank(nickName)) {
//                nickName = new String(nickName.getBytes("ISO-8859-1"), "UTF-8");
//            }

            User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .eq(User::getWxOpenid, openId));
            if (user != null) {
                if (user.getDeleted() == 1) {
                    return JsonResult.fail("账户出现异常，请联系客服");
                }
                user.setWxUserName(nickName);
                user.setWxHeadPic(avatarUrl);
                user.setLoginTime(new Date());
                //判断老用户第一次登录时间
                if (user.getAddDate() == null) {
                    user.setAddDate(new Date());
                }
                int resUser = userMapper.updateUserLogin(user);
                if (resUser <= 0) {
                    return JsonResult.fail("更新用户失败");
                }
            } else {
                String ip = HttpUtil.getIPAddress(req);
                user = new User();
                user.setWxOpenid(openId);
                user.setWxUserName(nickName);
                user.setWxHeadPic(avatarUrl);
                user.setMemberState(MemberState.Common.getCode());
                user.setAddDate(new Date());
                user.setAddUser(nickName);
                // user.setYaoCount(1);
                user.setIp(ip);
                user.setLoginTime(new Date());
                int resUser = userMapper.insert(user);
                if (resUser <= 0) {
                    return JsonResult.fail("登录失败");
                }
            }

            UserVO loginUser = insertUserToken(user);
            return JsonResult.ok(loginUser);
        }

        return JsonResult.fail("网络错误");
    }

    /**
     * 获取用户手机号通过微信授权,并且认证
     *
     * @param code
     * @param encryptedDataBase64
     * @param ivBase64
     * @param req
     * @return JsonResult<com.yitu.women.api.dto.LoginUser>
     * @author yaoyanhua
     * @date 2021/1/21 21:53
     */
    @Override
    public JsonResult<String> getPhoneAndAuthByWxAuth(String code, String encryptedDataBase64, String ivBase64, HttpServletRequest req) throws IOException {
        UserVO userVO = loginUserUtil.getLoginUser();
        if (userVO == null) {
            return JsonResult.fail("请登录");
        }
        final String _appId = URLEncoder.encode(appId, "UTF-8");
        final String _appSecret = URLEncoder.encode(appSecret, "UTF-8");
        Map<String, String> params = new HashMap<>();
        params.put("appid", _appId);
        params.put("secret", _appSecret);
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        Response res = HttpUtil.postRequest(authCode2SessionUrl, HttpUtil.getBody(params));
        if (res != null && res.isSuccessful() && res.body() != null) {
            String result = res.body().string();
            if (StringUtils.isBlank(result)) {
                return JsonResult.fail("解析失败");
            }

            JSONObject obj = JSONObject.parseObject(result);
            final String errCode = obj.getString("errcode");
            if (!StringUtils.isBlank(errCode)) {
                // code 错误
                return JsonResult.fail(obj.getString("errmsg"));
            }

            final String openId = obj.getString("openid");
            final String sessionKeyBase64 = obj.getString("session_key");

            final String jsonText = TencentUtil.aesCbcDecode(Base64.decodeBase64(encryptedDataBase64),
                    Base64.decodeBase64(sessionKeyBase64), Base64.decodeBase64(ivBase64));
            final JSONObject phoneObject = JSONObject.parseObject(jsonText);
            if (phoneObject == null) {
                return JsonResult.fail("获取微信用户授权失败");
            }
            final String phoneNumber = phoneObject.getString("purePhoneNumber");
            if (StringUtils.isBlank(phoneNumber)) {
                return JsonResult.fail("获取微信手机号失败");
            }
            userMapper.updateUserWxPhone(openId, phoneNumber);
            //认证工会会员
            JsonResult<Boolean> check = tradeUnionService.tradeUnionAuth(phoneNumber);
            if (check.getStatus() != ResponseCode.OK.getCode()) {
                return JsonResult.fail("工会认证异常");
            }
            userVO.setWxPhone(phoneNumber);
            userVO.setPhone(phoneNumber);
            userVO.setMemberState(check.getData() ? 1 : 0);
            commonService.setLoginUser(userVO.getSessionId(), userVO);
            return JsonResult.ok(phoneNumber);
        }

        return JsonResult.fail("网络错误");
    }

    /**
     * 获取用户手机号通过微信授权
     *
     * @param code
     * @param encryptedDataBase64
     * @param ivBase64
     * @param req
     * @return JsonResult<com.yitu.women.api.dto.LoginUser>
     * @author yaoyanhua
     * @date 2021/1/21 21:53
     */
    @Override
    public JsonResult<String> getPhoneByWxAuth(String code, String encryptedDataBase64, String ivBase64,
                                               HttpServletRequest req) throws IOException {
        final String _appId = URLEncoder.encode(appId, "UTF-8");
        final String _appSecret = URLEncoder.encode(appSecret, "UTF-8");
        Map<String, String> params = new HashMap<>();
        params.put("appid", _appId);
        params.put("secret", _appSecret);
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        Response res = HttpUtil.postRequest(authCode2SessionUrl, HttpUtil.getBody(params));
        if (res != null && res.isSuccessful() && res.body() != null) {
            String result = res.body().string();
            if (StringUtils.isBlank(result)) {
                return JsonResult.fail("解析失败");
            }

            JSONObject obj = JSONObject.parseObject(result);
            final String errCode = obj.getString("errcode");
            if (!StringUtils.isBlank(errCode)) {
                // code 错误
                return JsonResult.fail(obj.getString("errmsg"));
            }

            final String openId = obj.getString("openid");
            final String sessionKeyBase64 = obj.getString("session_key");

            final String jsonText = TencentUtil.aesCbcDecode(Base64.decodeBase64(encryptedDataBase64),
                    Base64.decodeBase64(sessionKeyBase64), Base64.decodeBase64(ivBase64));
            final JSONObject phoneObject = JSONObject.parseObject(jsonText);
            if (phoneObject == null) {
                return JsonResult.fail("获取微信用户授权失败");
            }
            final String phoneNumber = phoneObject.getString("purePhoneNumber");
            if (StringUtils.isBlank(phoneNumber)) {
                return JsonResult.fail("获取微信手机号失败");
            }
            return JsonResult.ok(phoneNumber);
        }

        return JsonResult.fail("网络错误");
    }

    private UserVO insertUserToken(User user) {
        final byte[] bytes = (System.currentTimeMillis() + "" + user.getId()).getBytes();
        final String token = UUID.nameUUIDFromBytes(bytes).toString().replaceAll("-", "").toUpperCase();
        UserVO loginUser = new UserVO();
        BeanUtils.copyProperties(user, loginUser);
        loginUser.setSessionId(token);
        //获取用户中奖记录
        List<Long> userPrizeIds = userPrizeMapper.getUserPrizeCount(loginUser.getId());
        if (userPrizeIds.size() > 0) {
            loginUser.setUserWinningPrizeId(StringUtils.join(userPrizeIds, ","));
        }
        commonService.setLoginUser(token, loginUser);
        //初始化用户摇一摇次数
        commonService.getUserShakeCount(loginUser.getId(), loginUser.getSessionId());
        return loginUser;
    }


    /**
     * 根据工会手机号获取用户信息
     *
     * @param phone
     * @return void
     * @author jxc
     * @date 2021/1/25 18:26
     */
    @Override
    public User getUserByPhone(String phone) {
        return baseMapper.getUserByPhone(phone);
    }

    /**
     * 工会认证成功修改
     *
     * @param userId
     * @param phone
     * @param memberState
     * @return int
     * @author jxc
     * @date 2021/1/26 9:04
     */
    @Override
    public boolean updateUserById(long userId, String phone, int memberState) {
        return baseMapper.updateUserById(userId, phone, memberState) > 0;
    }

    /**
     * 更新用户位置信息
     *
     * @param area 区域
     * @param lng  经度
     * @param lat  维度
     * @return JsonResult<java.lang.String>
     * @author pangshihe
     * @date 2021/1/29 11:51
     */
    @Override
    public JsonResult<String> updateUserLocation(String area, String lng, String lat) {
        if (area.length() > 100) {
            return JsonResult.fail("位置信息1-100个字");
        }
        if (lng.length() > 50) {
            return JsonResult.fail("经度1-50个字符");
        }
        if (lat.length() > 50) {
            return JsonResult.fail("维度1-50个字符");
        }
        int reslut = userMapper.updateUserLocation(loginUserUtil.getUserId(), area, lng, lat);
        return reslut > 0 ? JsonResult.ok() : JsonResult.fail("更新用户位置信息错误");
    }

    /**
     * 查询总记录数
     *
     * @param
     * @return java.lang.Integer
     * @author qixinyi
     * @date 2021/6/8 10:11
     */
    @Override
    public Integer findCount() {
        return userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getDeleted, DeletedEnum.NOT_DELETE.getValue()).eq(User::getMemberState, MemberStateEnum.RULES.getCode()));
    }

    /**
     * 获取我的页面详情
     *
     * @param
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.UserDetailVO>
     * @author qixinyi
     * @date 2021/6/10 14:33
     */
    @Override
    public JsonResult<UserDetailVO> findUserDetails() {
        // 获取登录用户
        UserVO loginUser = loginUserUtil.getLoginUser();

        UserDetailVO userDetailVO = new UserDetailVO();
        if (loginUser != null) {
            // 获取id
            Long id = loginUser.getId();
            User user = userMapper.selectById(id);
            Integer teamId = user.getTeamId();

            // 封装用户个人数据
            Integer scoreTotal = user.getScoreTotal();
            if (scoreTotal != null && scoreTotal > 0) {
                userDetailVO.setScoreTotal(user.getScoreTotal());
            } else {
                userDetailVO.setScoreTotal(0);
            }
            userDetailVO.setWxHeadPic(user.getWxHeadPic());
            userDetailVO.setId(id);

            JsonResult activeTimeJson = prizeBookService.getActiveTime(ActiveEntranceEnum.USER_SCORE.getValue());
            if (activeTimeJson.getStatus() == ResponseCode.INTERNAL_SERVER_ERROR.getCode()) {
                return JsonResult.fail(activeTimeJson.getMsg());
            }
            ActiveTimeVo activeTimeVo = (ActiveTimeVo) activeTimeJson.getData();
            Integer status = activeTimeVo.getStatus();

            if (status == ActiveStatusEnum.BEGIN.getValue()) {
                // 活动开始前
                userDetailVO.setUserRanking(0);
                userDetailVO.setTeamRanking(0);
                userDetailVO.setUserWeekRanking(0);
                userDetailVO.setAnswerRanking(0);
                userDetailVO.setTeamWeekRanking(0);

                return JsonResult.ok(userDetailVO);
            }

            Gson gson = new Gson();

            // 判断键值是否存在
            boolean userScoreKey = redisUtil.hHasKey(Configs.REDIS_ACTIVE_USER_SORT, id.toString());
            if (userScoreKey) {
                String userScoreJson = redisUtil.hget(Configs.REDIS_ACTIVE_USER_SORT, id.toString()).toString();
                if (!StringUtils.isEmpty(userScoreJson)) {
                    UserRankingListVO userScore = gson.fromJson(userScoreJson, UserRankingListVO.class);
                    // 获取用户总榜排名
                    userDetailVO.setUserRanking(userScore.getUserSort());
                }
            }else {
                userDetailVO.setUserRanking(0);
            }

            boolean userWeekScoreKey = redisUtil.hHasKey(Configs.REDIS_ACTIVE_USER_WEEK_SORT, id.toString());
            if (userWeekScoreKey) {
                String userWeekScoreJson = redisUtil.hget(Configs.REDIS_ACTIVE_USER_WEEK_SORT, id.toString()).toString();
                if (!StringUtils.isEmpty(userWeekScoreJson)) {
                    UserRankingListVO userWeekScore = gson.fromJson(userWeekScoreJson, UserRankingListVO.class);
                    // 获取用户周榜排名
                    userDetailVO.setUserWeekRanking(userWeekScore.getUserSort());
                }
            }else {
                userDetailVO.setUserWeekRanking(0);
            }

            boolean teamScoreKey = redisUtil.hHasKey(Configs.REDIS_ACTIVE_TEAM_SORT, teamId.toString());
            if (teamScoreKey) {
                String teamScoreJson = redisUtil.hget(Configs.REDIS_ACTIVE_TEAM_SORT, teamId.toString()).toString();
                if (!StringUtils.isEmpty(teamScoreJson)) {
                    TeamRankingListVO teamScore = gson.fromJson(teamScoreJson, TeamRankingListVO.class);
                    // 获取团队总榜排名
                    userDetailVO.setTeamRanking(teamScore.getTeamSort());
                }
            }else {
                userDetailVO.setTeamRanking(0);
            }

            boolean teamWeekScoreKey = redisUtil.hHasKey(Configs.REDIS_ACTIVE_TEAM_WEEK_SORT, teamId.toString());
            if (teamWeekScoreKey) {
                String teamWeekScoreJson = redisUtil.hget(Configs.REDIS_ACTIVE_TEAM_WEEK_SORT, teamId.toString()).toString();
                if (!StringUtils.isEmpty(teamWeekScoreJson)) {
                    TeamRankingListVO teamWeekScore = gson.fromJson(teamWeekScoreJson, TeamRankingListVO.class);
                    // 获取团队周榜排名
                    userDetailVO.setTeamWeekRanking(teamWeekScore.getTeamSort());
                }
            }else {
                userDetailVO.setTeamWeekRanking(0);
            }

            boolean answerScoreKey = redisUtil.hHasKey(Configs.REDIS_ACTIVE_ANSWER_SORT, id.toString());
            if (answerScoreKey) {
                String answerScoreJson = redisUtil.hget(Configs.REDIS_ACTIVE_ANSWER_SORT, id.toString()).toString();

                if (!StringUtils.isEmpty(answerScoreJson)) {
                    UserRankingListVO answerScore = gson.fromJson(answerScoreJson, UserRankingListVO.class);
                    // 获取答题榜排名
                    userDetailVO.setAnswerRanking(answerScore.getUserSort());
                }
            }else {
                userDetailVO.setAnswerRanking(0);
            }

            // 封装活动时间
            DatePo weekDate = null;
            if (status == ActiveStatusEnum.ING.getValue()) {
                // 活动中
                weekDate = weekRankingUtil.getWeekDate();
            } else if (status == ActiveStatusEnum.END.getValue()) {
                // 活动结束
                weekDate = weekRankingUtil.getLastWeek();
            }
            if (weekDate != null) {
                String begin = weekDate.getBeginDate().substring(5, 10).replace("-", ".").replaceFirst("0", "");
                String end = weekDate.getEndDate().substring(5, 10).replace("-", ".").replaceFirst("0", "");
                userDetailVO.setTime(begin + "-" + end);
            }
        }

        return JsonResult.ok(userDetailVO);
    }

    /**
     * 根据id查找用户
     *
     * @param id
     * @return com.yitu.cpcFounding.api.domain.User
     * @author qixinyi
     * @date 2021/6/11 16:52
     */
    @Override
    public User findUserById(Long id) {
        return userMapper.selectById(id);
    }

    /**
     * 刷新会员认证
     *
     * @param
     * @return com.yitu.cpcFounding.api.vo.JsonResult
     * @author shenjun
     * @date 2021-6-15 17:48:20
     */
    @Override
    public JsonResult refreshMember() {
        UserVO userVO = loginUserUtil.getLoginUser();
        if (userVO.getMemberState() == MemberState.Member.getCode()) {
            return JsonResult.fail("当前用户已为工会会员，请勿重复刷新");
        }
        if (userVO == null) {
            return JsonResult.fail("请登录");
        }
        if (StringUtils.isBlank(userVO.getWxPhone())) {
            return JsonResult.fail("wx手机号不存在");
        }
        //认证工会会员
        JsonResult<Boolean> check = tradeUnionService.tradeUnionAuth(userVO.getWxPhone());
        if (check.getStatus() != ResponseCode.OK.getCode()) {
            return JsonResult.fail(check.getMsg());
        }
        if (!check.getData()) {
            return JsonResult.fail("当前手机号不是工会会员，请认证完成再刷新");
        }
        userVO.setMemberState(MemberState.Member.getCode());
        commonService.setLoginUser(userVO.getSessionId(), userVO);
        return JsonResult.ok("当前手机号认证工会会员成功");
    }

    /**
     * 获取用户信息
     *
     * @return JsonResult<com.yitu.women.api.dto.LoginUser>
     * @author shenjun
     * @date 2021/1/21 21:55
     */
    @Override
    public JsonResult<UserVO> getUserInfo() {
        UserVO userVO = loginUserUtil.getLoginUser();
        if (userVO == null) {
            return JsonResult.fail("用户未登录");
        } else {
            return JsonResult.ok(userVO);
        }
    }
}
