package com.yitu.cpcFounding.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


/**
 * AES工具类
 * @author liuzhaowei
 * @date 2020/9/10
 */
public class AESUtils {
    private static Logger logger = LoggerFactory.getLogger(AESUtils.class);

    private static final String CHARSET = "UTF-8";
    public static final String USER_CERTNO_KEY="szsti@#%k4bd9652";

    public static String encrypt(String content, String key) {
        try {
            byte[] raw = key.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(1, skeySpec);
            byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
            //  String miwen = new String(Base64.encode(encrypted), "UTF-8");
            return Base64.getEncoder().encodeToString(encrypted);
//            String miwen =new BASE64Encoder().encode(encrypted);
//            return miwen;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return null;
    }

    public static String decrypt(String content, String key) {
        try {
            content = content.replaceAll("\r?\n?", "");
            byte[] raw = key.getBytes("UTF-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(2, skeySpec);
            byte[] encrypted1 =Base64.getDecoder().decode(content); // Base64.decode(content);
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "UTF-8").trim();
            return originalString;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }

        return null;
    }
}
