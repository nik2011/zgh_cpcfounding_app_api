package com.yitu.cpcFounding.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yitu.cpcFounding.api.constant.Configs;
import com.yitu.cpcFounding.api.service.TradeUnionService;
import com.yitu.cpcFounding.api.auth.LoginUserUtil;
import com.yitu.cpcFounding.api.domain.GhSmBasicSituation;
import com.yitu.cpcFounding.api.domain.User;
import com.yitu.cpcFounding.api.enums.MemberState;
import com.yitu.cpcFounding.api.enums.ResponseCode;
import com.yitu.cpcFounding.api.mapper.UserMapper;
import com.yitu.cpcFounding.api.redis.RedisUtil;
import com.yitu.cpcFounding.api.service.GhSmBasicSituationService;
import com.yitu.cpcFounding.api.service.UserService;
import com.yitu.cpcFounding.api.service.SMSSevice;
import com.yitu.cpcFounding.api.utils.RegexUtil;
import com.yitu.cpcFounding.api.utils.AxisUtil;
import com.yitu.cpcFounding.api.utils.DessUtils;
import com.yitu.cpcFounding.api.vo.AuthSuccessDetailVo;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.axis.Constants;
import org.apache.axis.client.Call;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;


/**
 * 工会认证服务实现层
 *
 * @author jxc
 * @date 2021/1/23
 */
@Slf4j
@Service
public class TradeUnionServiceImpl implements TradeUnionService {

    /**
     * 工会认证秘钥
     */
    @Value("${tradeUnion.key}")
    private String key;
    /**
     * 服务单位标号
     */
    @Value("${tradeUnion.identity}")
    private String identity;
    /**
     * 角色
     */
    @Value("${tradeUnion.role}")
    private String role;
    /**
     * 用户名
     */
    @Value("${tradeUnion.username}")
    private String username;
    /**
     * 密码
     */
    @Value("${tradeUnion.password}")
    private String password;

    @Value("${tradeUnion.endpoint}")
    private String endpoint;

    @Value("${tradeUnion.targetNamespace}")
    private String targetNamespace;

    @Value("${tradeUnion.method}")
    private String method;
    @Resource
    private UserService userService;
    @Resource
    private LoginUserUtil loginUserUtil;
    @Resource
    private SMSSevice smsSevice;
    @Resource
    private CommonServiceImpl commonService;
    @Resource
    private GhSmBasicSituationService ghSmBasicSituationService;
    @Resource
    private UserMapper userMapper;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 工会认证
     *
     * @param phone
     * @return JsonResult<java.lang.Boolean>
     * @author shenjun
     * @date 2021-4-8 18:08:45
     */
    //@Transactional
    public JsonResult<Boolean> tradeUnionAuth(String phone) {
        // 手机号是否认证其他用户
        final UserVO userVO = loginUserUtil.getLoginUser();
        if (userVO.getMemberState() == MemberState.Member.getCode()) {
            return JsonResult.fail("该用户已认证");
        }
        Long currentUserId = userVO.getId();
        User userRecord = userService.getUserByPhone(phone);
        if (userRecord != null) {
            if (currentUserId != userRecord.getId()) {
                return JsonResult.fail("该手机号已认证其他用户");
            }
            if (userRecord.getMemberState() == MemberState.Member.getCode()) {
                return JsonResult.fail("该手机号已认证");
            }
        }
        // 查询工会表，不存在调用工会认证接口
        GhSmBasicSituation ghSmBasicSituationCond = new GhSmBasicSituation();
        ghSmBasicSituationCond.setContactPhone(phone);
        QueryWrapper queryWrapper = new QueryWrapper(ghSmBasicSituationCond);
        queryWrapper.last("LIMIT 1");
        GhSmBasicSituation ghSmBasicSituation = ghSmBasicSituationService.getOne(queryWrapper);
        if (ghSmBasicSituation == null) {
            JsonResult result = this.requestAuth(phone);
            Boolean data = (Boolean) result.getData();
            if (result.getStatus() != ResponseCode.OK.getCode() || !data) {
                //验证失败，手机号只填到wx手机号上
                userMapper.updateUserWxPhone(userVO.getWxOpenid(), phone);
                return result;
            }
        }
        boolean result = userService.updateUserById(currentUserId, phone, MemberState.Member.getCode());
        return result?JsonResult.ok(true): JsonResult.fail("修改认证信息失败");

//        if (userService.updateUserById(currentUserId, phone, MemberState.Member.getCode())) {
//            //增加一次摇一摇机会
//            // DynamicUserVO dynamicUserVO = commonService.getUserDynamicInfo(currentUserId, userVO.getSessionId());
//            commonService.incrOneUserYaoCount(currentUserId, userVO.getSessionId());
//            //判断今日是否已送完奖章 如果送过再添加一次摇一摇机会
//            Object obj = redisUtil.hget(Configs.REDIS_USER_MEDAL_COUNT, currentUserId.toString());
//            if (obj != null && (int) obj >= 3) {
//                //增加一次摇一摇机会
//                commonService.incrOneUserYaoCount(currentUserId, userVO.getSessionId());
//            }
//            userVO.setMemberState(MemberState.Member.getCode());
//            commonService.setLoginUser(userVO.getSessionId(), userVO);
//            return JsonResult.ok(true);
//        } else {
//            return JsonResult.fail("修改认证信息失败");
//        }
    }

    /**
     * 工会认证
     *
     * @param phone
     * @return JsonResult<java.lang.Boolean>
     * @author jxc
     * @date 2021/1/23 10:48
     */
    @Override
    public JsonResult<Boolean> tradeUnionAuth(String phone, String code) {


        // 验证手机号
        if (!RegexUtil.validateMobilePhone(phone)) {
            return JsonResult.fail("手机号码格式不正确");
        }

        // 短信验证
        JsonResult jsonResult = this.smsSevice.verifiCode(phone, code);
        if (jsonResult.getStatus() != ResponseCode.OK.getCode()) {
            return jsonResult;
        }

        JsonResult<Boolean> check = tradeUnionAuth(phone);
        if (check.getStatus() != ResponseCode.OK.getCode()) {
            return check;
        }
        UserVO userVO = loginUserUtil.getLoginUser();
        userVO.setWxPhone(phone);
        userVO.setPhone(phone);
        userVO.setMemberState(check.getData() ? 1 : 0);
        commonService.setLoginUser(userVO.getSessionId(), userVO);
        return check;
    }

    /**
     * 调用工会接口
     *
     * @param phone
     * @return JsonResult
     * @author jxc
     * @date 2021/1/26 9:08
     */
    @Override
    public JsonResult<Boolean> requestAuth(String phone) {
        try {
            // 指出service所在URL
//            java.lang.String endpoint = "http://app.szzgh.org/services/memberValidateService?wsdl";
//            java.lang.String targetNamespace = "http://impl.criterion.bus.common.css.com";
//            java.lang.String method = "isMember";

            // 创建一个服务(service)调用(call)
            org.apache.axis.client.Service service = new org.apache.axis.client.Service();
            // 通过service创建call对象
            Call call = (Call) service.createCall();

            // 设置service所在URL
            call.setTargetEndpointAddress(new java.net.URL(endpoint));
            call.setOperationName(new QName(targetNamespace, method));
            call.addHeader(AxisUtil.getSoapHeader(role, username, password));
            call.addParameter(new QName(null, "parm", "impl"), Constants.XSD_INT, ParameterMode.IN);
            call.setUseSOAPAction(true);
            call.setReturnType(org.apache.axis.encoding.XMLType.XSD_STRING);

            JSONObject params = new JSONObject();
            params.put("identity", identity);
            params.put("tel", phone);
            Object ret = call.invoke(new Object[]{DessUtils.encypt(params.toJSONString(), key)});

            JSONObject object = JSONObject.parseObject(ret.toString());
            if (phone.equals("13266583211")) {
//                return JsonResult.ok(true);
            }
            // 是否认证
            boolean sfsm = object.getBoolean("sfsm");
            if (sfsm) {
                return JsonResult.ok(true);
            } else {
                return JsonResult.ok("您还不是工会会员", false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("工会认证接口调用失败");
        }
    }

    /**
     * 认证成功详情
     *
     * @param
     * @return JsonResult<AuthSuccessDetailVo>
     * @author jxc
     * @date 2021/1/26 11:18
     */
    @Override
    public JsonResult<AuthSuccessDetailVo> getAuthSuccessDetail() {
        Long userId = loginUserUtil.getUserId();
        User user = userService.getById(userId);
        AuthSuccessDetailVo authSuccessDetailVo = new AuthSuccessDetailVo();
        authSuccessDetailVo.setMemberState(user.getMemberState());
        authSuccessDetailVo.setPhone(user.getPhone());
        if (user.getMemberState() == MemberState.Common.getCode()) {
            authSuccessDetailVo.setTip("认证未通过，您还不是工会实名认证会员");
        } else if (user.getMemberState() == MemberState.Member.getCode()) {
            authSuccessDetailVo.setTip("认证通过，是工会实名认证会员");
        }
        return JsonResult.ok(authSuccessDetailVo);
    }
}




