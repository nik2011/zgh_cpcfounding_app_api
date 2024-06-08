package com.yitu.cpcFounding.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yitu.cpcFounding.api.constant.WxPayUrl;
import com.yitu.cpcFounding.api.exception.ConsciousException;
import com.yitu.cpcFounding.api.utils.HttpUtil;
import com.yitu.cpcFounding.api.auth.LoginUserUtil;
import com.yitu.cpcFounding.api.domain.PrizeBook;
import com.yitu.cpcFounding.api.domain.UserPrize;
import com.yitu.cpcFounding.api.enums.OperationLogEnum;
import com.yitu.cpcFounding.api.enums.prize.PrizeTypeEnum;
import com.yitu.cpcFounding.api.enums.prize.UserPrizeStatusEnum;
import com.yitu.cpcFounding.api.mapper.PrizeBookMapper;
import com.yitu.cpcFounding.api.mapper.UserPrizeMapper;
import com.yitu.cpcFounding.api.service.OperationLogService;
import com.yitu.cpcFounding.api.service.WxVoucherService;
import com.yitu.cpcFounding.api.wxpay.WxTokenUtil;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信代金券service
 * @author chenpinjia
 * @version 1.0
 * @date 2021/1/23
 */
@Slf4j
@Service
public class WxVoucherServiceImpl implements WxVoucherService {

    @Resource
    private LoginUserUtil loginUserUtil;

    @Resource
    private WxTokenUtil wxTokenUtil;

    @Resource
    private UserPrizeMapper userPrizeMapper;

    @Resource
    private PrizeBookMapper prizeBookMapper;

    @Resource
    private OperationLogService operationLogService;

    @Value("${wechat.appId}")
    private String appId;


    @Value("${wechat.mchid}")
    private String mchid;

    /**
     * 发放代金券
     * @param id 用户奖品记录id
     * @return void
     * @author chenpinjia
     * @date 2021/1/26 20:24
     */
    @Override
    //@Transactional(rollbackFor = Throwable.class)
    public JsonResult sendVoucher(Long id) {
        UserPrize userPrize = userPrizeMapper.selectById(id);
        if (userPrize == null){
            return JsonResult.fail("未找到的中奖记录");
        }
        PrizeBook prize = prizeBookMapper.selectById(userPrize.getPrizeId());
        if (prize == null){
            return JsonResult.fail("未找到的奖品类型");
        }
        if (prize.getPrizeType() != PrizeTypeEnum.VOUCHER.getCode()) {
            return JsonResult.fail("奖品类型不是代金卷");
        }
        //接口批次号
        String stockId = prize.getBatchNo();
        if (StringUtils.isBlank(stockId)){
            return JsonResult.fail("奖品尚未配置批次号");
        }
        //商户单据号 （商户侧唯一，取奖品订单号）
        String outRequestNo = userPrize.getOrderNo();
        if (StringUtils.isBlank(outRequestNo)){
            return JsonResult.fail("未生成正常的订单号");
        }
        UserVO userVO = loginUserUtil.getLoginUser();
        if (userPrize.getUserId().longValue() != userVO.getId().longValue()){
            return JsonResult.fail("不属于当前用户的奖品");
        }
        String openId = userVO.getWxOpenid();
        String url = String.format(WxPayUrl.SEND_VOUCHER, openId);
        Map<String, Object> param = new HashMap<>(4);
        param.put("stock_id",stockId);
        param.put("out_request_no",outRequestNo);
        param.put("appid",appId);
        param.put("stock_creator_mchid",mchid);
        String sendJson = JSONObject.toJSONString(param);
        //调用微信支付接口
        String returnJson = useWxInterface(url, sendJson);
        JSONObject reJsonObj = JSONObject.parseObject(returnJson);

        String couponId = reJsonObj.getString("coupon_id");
        if (StringUtils.isBlank(couponId)){
            saveWxLog(url, sendJson, returnJson);
            return JsonResult.fail("代金券发放失败");
        }
        //发放成功之后将返回的id存入兑换码字段, 并更新状态
        Date now  = new Date();
        userPrize.setExchangeCode(couponId);
        userPrize.setStatus(UserPrizeStatusEnum.EXCHANGED.getCode());
        userPrize.setGetTime(now);
        userPrize.setModifyUser(userVO.getWxUserName());
        userPrize.setModifyDate(now);
        userPrize.updateById();

        //操作完成后记录
        saveWxLog(url, sendJson, returnJson);
        return JsonResult.ok();
    }


    /**
     * 调用微信支付接口
     * @param wxUrl 连接
     * @param jsonBody 发送的json
     * @return java.lang.String
     * @author chenpinjia
     * @date 2021/1/26 20:15
     */
    private String useWxInterface(String wxUrl ,String jsonBody){
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonBody);

        String authorization = null;
        try {
            authorization = wxTokenUtil.getToken("POST", wxUrl, jsonBody);
            log.info("authorization："+authorization);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            throw new ConsciousException("签名生成失败");
        }
        Headers headers = new Headers.Builder()
                .add("Content-Type", "application/json")
                .add("Accept", "application/json")
                .add("Authorization", authorization)
                .build();
        Response response = HttpUtil.postRequest(wxUrl,body, headers);
        String responseBody;
        try {
            responseBody = response.body().string();
            log.info("responseBody："+responseBody);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            throw new ConsciousException("微信接口调用失败");
        }
        return responseBody;
    }

    /**
     * 保存调用微信接口的日志
     * @param url
     * @param sendJson
     * @param returnJson
     * @return void
     * @author chenpinjia
     * @date 2021/2/3 16:17
     */
    private void saveWxLog(String url, String sendJson, String returnJson){
        Map<String, String> log = new HashMap<>(3);
        log.put("url", url);
        log.put("sendJson", sendJson);
        log.put("returnJson", returnJson);
        operationLogService.addOperationLog(null, OperationLogEnum.SEND_VOUCHER, JSONObject.toJSONString(log));
    }


}
