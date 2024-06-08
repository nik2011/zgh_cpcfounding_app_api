package com.yitu.cpcFounding.api.constant;

/**
 * 微信支付接口地址
 * @author chenpinjia
 * @version 1.0
 * @date 2021/1/26
 */
public class WxPayUrl {

    /**
     * 激活代金券批次API
     * %s 批次号 stock_id：微信为每个代金券批次分配的唯一id。
     */
    public final static String ACTIVE_VOUCHER = "https://api.mch.weixin.qq.com/v3/marketing/favor/stocks/%s/start";


    /**
     * 发放代金券API
     * %s 用户openid  openid：openid信息，用户在appid下的唯一标识。
     */
    public final static String SEND_VOUCHER = "https://api.mch.weixin.qq.com/v3/marketing/favor/users/%s/coupons";
}
