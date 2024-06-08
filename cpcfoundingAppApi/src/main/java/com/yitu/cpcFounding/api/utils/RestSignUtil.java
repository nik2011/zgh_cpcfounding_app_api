package com.yitu.cpcFounding.api.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @author yanhua
 * @Description:
 * @date 2019/1/5
 */
public class RestSignUtil {
    
    /**
    * @Description: 拼成请求字符串
    * @param
    * @return 
    * @author yanhua
    * @date 2019/1/5
    */
    public static String mapToUrlString(Map<String, String> paramsMap) {
        TreeMap<String, String> orderedParams = new TreeMap<>(paramsMap);
        final Set<Map.Entry<String, String>> entries = orderedParams.entrySet();
        final List<String> list = new ArrayList<>(orderedParams.size());
        for (Map.Entry<String, String> entry : entries) {
            final String key = entry.getKey();
            final Object value = entry.getValue();
            if (StringUtils.equals(key, "signature") || value == null) {
                continue;
            }
            list.add(key + "=" + value);
        }
        return StringUtils.join(list, "&");
    }


    /**
    * @Description: 签名
    * @param
    * @return 
    * @author yanhua
    * @date 2019/1/5
    */
    public static String sign(String signStr, String signType, String appSecret) {
        String signatureActual = "";
        if (StringUtils.equals("MD5", signType)) {
            signatureActual = DigestUtils.md5Hex(signStr + appSecret);
        }
        else if (StringUtils.equals("SHA1", signType)) {
            signatureActual = DigestUtils.sha1Hex(signStr + appSecret);
        }
        return signatureActual;
    }
}
