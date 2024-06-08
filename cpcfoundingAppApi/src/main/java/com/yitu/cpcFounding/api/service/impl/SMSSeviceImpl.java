package com.yitu.cpcFounding.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yitu.cpcFounding.api.auth.LoginUserUtil;
import com.yitu.cpcFounding.api.constant.Configs;
import com.yitu.cpcFounding.api.domain.Msg;
import com.yitu.cpcFounding.api.enums.ResponseCode;
import com.yitu.cpcFounding.api.enums.SMSEnum;
import com.yitu.cpcFounding.api.mapper.ShareMsgMapper;
import com.yitu.cpcFounding.api.redis.RedisUtil;
import com.yitu.cpcFounding.api.service.SMSSevice;
import com.yitu.cpcFounding.api.utils.*;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.UserVO;
import com.yitu.cpcFounding.api.vo.sms.SMSParamVO;
import com.yitu.cpcFounding.api.vo.sms.SMSSendStatusVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 创建人
 * LsYearAdminWebSystem
 * 2021/1/25 18:13
 *
 * @author luzhichao
 **/
@Service
public class SMSSeviceImpl implements SMSSevice {

    private static Logger log = LoggerFactory.getLogger(SMSSeviceImpl.class);

    @Value("${sms.url}")
    private String url;

    @Value("${sms.username}")
    private String username;

    @Value("${sms.password}")
    private String password;

    @Resource
    RedisUtil redisUtil;

    @Autowired
    private LoginUserUtil loginUserUtil;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    ShareMsgMapper shareMsgMapper;

    /**
     * @description: 发送短信验证码
     * @param phone
     * @return: JsonResult
     * @author: luzhichao
     * @date: 2021/1/26 10:08
     */
    @Override
    public JsonResult sendVerifiCode(String phone) {
        if (!CommonUtil.isPhone(phone)){
            return JsonResult.fail("手机号格式错误！");
        }
        UserVO user = loginUserUtil.getLoginUser();
        if (user == null) {
            return JsonResult.build(ResponseCode.UNAUTHORIZED);
        }
        // 生成验证码
        String code = RandomValidateCodeUtil.generateNum(6);
        // 获取redis信息
        Object object = redisUtil.get(Configs.REDIS_SMS_PHONE + phone);
        SMSSendStatusVO smsSendStatusVO = null;
        if (object != null) {
            smsSendStatusVO = JSONObject.toJavaObject(JSONObject.parseObject(object.toString()), SMSSendStatusVO.class);
            if (smsSendStatusVO != null) {
                long second = (System.currentTimeMillis() - smsSendStatusVO.getLastTime().getTime()) / 1000;
                if (second < 60) {
                    return JsonResult.fail("操作频繁请稍后重试！");
                }
                if (smsSendStatusVO.getCount() >= Configs.MAX_SMS_PHONE_COUNT) {
                    return JsonResult.fail("当天发送次数已达上限！");
                }
            }
        }
        smsSendStatusVO = smsSendStatusVO == null ? new SMSSendStatusVO() : smsSendStatusVO;
        smsSendStatusVO.setPhone(phone);
        smsSendStatusVO.setUserId(user.getId().intValue());
        smsSendStatusVO.setCode(code);
        smsSendStatusVO.setCount(smsSendStatusVO.getCount() + 1);
        Date date = new Date();
        smsSendStatusVO.setLastTime(date);
        String content = String.format(Configs.SMS_MSG_TEMPLATE, code);
        JsonResult<String> result = sendMsg(content, phone);
        if (result.getStatus() == ResponseCode.OK.getCode()) {
            redisUtil.set(Configs.REDIS_SMS_PHONE + phone, JSONObject.toJSONString(smsSendStatusVO), DateTimeUtil.getDiffNextDaySeconds());
            // 保存数据
            Msg msg = new Msg();
            msg.setUserId(user.getId().intValue());
            msg.setMobilePhone(phone);
            msg.setContent(code);
            msg.setRemark(result.getData());
            msg.setSendTime(date);
            msg.setAddUser(user.getWxUserName());
            msg.setAddDate(new Date());
            msg.setIp(IpUtil.getIpAdrress(httpServletRequest));
            msg.setDeleted(0);
            shareMsgMapper.insert(msg);
        }
        return result;
    }

    /**
     * @description: 验证码校验
     * @param phone 手机号
     * @param code 验证码
     * @return: boolean
     * @author: luzhichao
     * @date: 2021/1/26 11:47
     */
    @Override
    public JsonResult<String> verifiCode(String phone, String code) {
        if (!CommonUtil.isPhone(phone)){
            return JsonResult.fail("手机号格式错误");
        }
        if (StringUtils.isEmpty(code)){
            return JsonResult.fail("验证码不能为空");
        }
        Object object = redisUtil.get(Configs.REDIS_SMS_PHONE + phone);
        if (object == null) {
            return JsonResult.fail("验证码已失效！");
        }
        SMSSendStatusVO smsSendStatusVO = JSONObject.toJavaObject(JSONObject.parseObject(object.toString()), SMSSendStatusVO.class);
        if (smsSendStatusVO != null) {
            long second = (System.currentTimeMillis() - smsSendStatusVO.getLastTime().getTime()) / 1000;
            // 判断验证码是否超过最大存活时间
            if (second <= Configs.MAX_SMS_CODE_TIME && StringUtils.isNotEmpty(smsSendStatusVO.getCode())) {
                if (StringUtils.equals(smsSendStatusVO.getCode(), code)) {
                    smsSendStatusVO.setCode(null);
                    redisUtil.set(Configs.REDIS_SMS_PHONE + phone, JSONObject.toJSONString(smsSendStatusVO), DateTimeUtil.getDiffNextDaySeconds());
                    return JsonResult.ok();
                } else {
                    return JsonResult.fail("验证码错误！");
                }
            }
        }
        return JsonResult.fail("验证码已失效！");
    }

    /**
     * @description: 发送短信
     * @param content  消息内容
     * @param phone 手机号 多个逗号分隔
     * @return: com.yitu.year.adminweb.vo.JsonResult
     * @author: luzhichao
     * @date: 2021/1/25 18:22
     */
    @Override
    public JsonResult<String> sendMsg(String content, String phone) {
        if (StringUtils.isEmpty(content)) {
            return JsonResult.fail("消息内容不能为空！");
        }
        if (StringUtils.isEmpty(phone)) {
            return JsonResult.fail("联系号码不能为空！");
        }
        SMSParamVO smsParamVO = new SMSParamVO();
        smsParamVO.setUid(username);
        //String passwordNew = DigestUtils.md5Hex(password);
        smsParamVO.setPsw(password);
        smsParamVO.setMobiles(phone);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        smsParamVO.setMsgid(uuid);
        try {
            smsParamVO.setMsg(URLEncoder.encode(content, "GBK"));
        } catch (UnsupportedEncodingException e) {
            log.error("编码错误！");
        }
        String result = HttpUtil.getRequest(url, (Map<String, Object>) JSONObject.toJSON(smsParamVO));
        // 解析返回参
        if (result != null) {
            if (Integer.parseInt(result) == SMSEnum.SUCCESS.getCode()) {
                return JsonResult.ok(uuid);
            } else {
                return JsonResult.fail(SMSEnum.getEnum(Integer.parseInt(result)).getMsg());
            }
        }
        return JsonResult.fail("请求失败！");
    }
}
