package com.yitu.cpcFounding.api.wxpay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenpinjia
 * @version 1.0
 * @date 2021/1/26
 */
@Configuration
public class WxTokenBeanConfig {

    @Value("${wechat.mchid}")
    private String mchid;


    @Bean
    public WxTokenUtil wxTokenUtil(){
        WxTokenUtil wxTokenUtil = new WxTokenUtil();
        wxTokenUtil.createPKCS12("/certificate/apiclient_cert.p12", "Tenpay Certificate", "1596921091");
        return wxTokenUtil;
    }
}
