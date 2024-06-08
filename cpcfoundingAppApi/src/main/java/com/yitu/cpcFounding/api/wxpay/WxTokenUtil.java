package com.yitu.cpcFounding.api.wxpay;

import okhttp3.HttpUrl;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

import java.security.cert.X509Certificate;

/**
 * 微信支付token工具类
 * @author chenpinjia
 * @version 1.0
 * @date 2021/1/26
 */
public class WxTokenUtil {

    // 证书的 公钥-私钥
    private KeyStore store;
    //证书序列号
    private String serialNumber;
    // 证书的 公钥-私钥
    private KeyPair keyPair;

    private String HEADER_KEY = "Authorization";

    private String SCHEMA = "WECHATPAY2-SHA256-RSA2048";

    private final Object lock = new Object();

    @Value("${wechat.mchid}")
    private String mchid;

    /**
     * 获取请求Authorization token
     * @param method 请求方式
     * @param url 请求url
     * @param body 请求体
     * @return java.lang.String
     * @author chenpinjia
     * @date 2021/1/26 14:43
     */
    public String getToken(String method, String url, String body) throws Exception{
        String nonceStr = RandomStringUtils.randomNumeric(10);
        long timestamp = System.currentTimeMillis() /1000;
        HttpUrl httpUrl = HttpUrl.parse(url);
        String message = buildMessage(method, httpUrl, timestamp, nonceStr, body);
        String signature = sign(message);

        return "WECHATPAY2-SHA256-RSA2048 mchid=\"" + mchid + "\","
                + "nonce_str=\"" + nonceStr + "\","
                + "timestamp=\"" + timestamp + "\","
                + "serial_no=\"" + serialNumber + "\","
                + "signature=\"" + signature + "\"";

    }


    /**
     * 获取签名
     * @param message 签名
     * @return java.lang.String
     * @author chenpinjia
     * @date 2021/1/26 14:41
     */
    public String sign(String message) throws Exception {
        Signature sign = Signature.getInstance("SHA256withRSA");
//        sign.initSign("yourPrivateKey");
        sign.initSign(keyPair.getPrivate());
        sign.update(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(sign.sign());
    }

    /**
     * 获取签名串
     * @param method http请求方法 （GET,POST 等）
     * @param url 请求的地址
     * @param timestamp 时间戳
     * @param nonceStr 随机字符串
     * @param body 请求体
     *             请求方法为GET时，报文主体为空。
     *             当请求方法为POST或PUT时，请使用真实发送的JSON报文。
     *             图片上传API，请使用meta对应的JSON报文。
     * @return java.lang.String
     * @author chenpinjia
     * @date 2021/1/26 14:35
     */
    public String buildMessage(String method, HttpUrl url,long timestamp,String nonceStr,  String body) {
        String canonicalUrl = url.encodedPath();
        if (url.encodedQuery() != null) {
            canonicalUrl += "?" + url.encodedQuery();
        }
        return method + "\n"
                + canonicalUrl + "\n"
                + timestamp + "\n"
                + nonceStr + "\n"
                + body + "\n";
    }



    /**
     * 获取公私钥.
     *
     * @param keyPath  the key path
     * @param keyAlias the key alias
     * @param keyPass  password
     * @return the key pair
     */
    public KeyPair createPKCS12(String keyPath, String keyAlias, String keyPass) {
        ClassPathResource resource = new ClassPathResource(keyPath);
        char[] pem = keyPass.toCharArray();
        try {
            synchronized (lock) {
                if (store == null) {
                    synchronized (lock) {
                        store = KeyStore.getInstance("PKCS12");
                        store.load(resource.getInputStream(), pem);
                    }
                }
            }
            X509Certificate certificate = (X509Certificate) store.getCertificate(keyAlias);
            certificate.checkValidity();
            // 证书的序列号 也有用
            String serialNumber = certificate.getSerialNumber().toString(16).toUpperCase();

            this.serialNumber = serialNumber;
            // 证书的 公钥
            PublicKey publicKey = certificate.getPublicKey();
            // 证书的私钥
            PrivateKey storeKey = (PrivateKey) store.getKey(keyAlias, pem);


            keyPair = new KeyPair(publicKey, storeKey);
            return keyPair;

        } catch (Exception e) {
            throw new IllegalStateException("Cannot load keys from store: " + resource, e);
        }
    }
}
