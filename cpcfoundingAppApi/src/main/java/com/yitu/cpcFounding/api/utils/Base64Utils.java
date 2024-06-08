package com.yitu.cpcFounding.api.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

/**
 * Base64编码解码
 *
 * @author malidong
 * @date 2020/1/3 10:16
 */
public class Base64Utils {

    private static final Logger log = LoggerFactory.getLogger(Base64Utils.class);

    /**
     * 编码
     *
     * @param str
     * @return
     */
    public static String encoder(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        Base64.Encoder encoder = Base64.getEncoder();
        try {
            return encoder.encodeToString(str.getBytes("UTF-8"));
        } catch (Exception e) {
            log.error("Base64编码失败：", e);
            return null;
        }
    }

    /**
     * 解码
     *
     * @param str
     * @return
     */
    public static String decode(String str) {
        if (StringUtils.isBlank(str)) {
            return "";
        }
        Base64.Decoder decoder = Base64.getDecoder();
        try {
            return new String(decoder.decode(str), "UTF-8");
        } catch (Exception e) {
            log.error("Base64解码失败：", e);
            return null;
        }
    }
}
