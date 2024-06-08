package com.yitu.cpcFounding.api.utils;


import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;


/**
 * @Description:
 * @author yaoyanhua
 * @date 2021/01/21
 * @param
 * @return
 */
public final class TencentUtil {
    private static final String CBC_CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String KEY_ALGORITHM = "AES";
    private static final Logger log = LoggerFactory.getLogger(TencentUtil.class);

    /**
     * 生成 Authorization 签名字段
     *
     * @param appId     appId
     * @param secretId  secretId
     * @param secretKey secretKey
     * @param expired   expired
     * @return 结果
     * @throws Exception 异常
     */
    public static String appSign(long appId, String secretId, String secretKey, long expired) throws Exception {
        long now = System.currentTimeMillis() / 1000;
        int rdm = Math.abs(new Random().nextInt());
        String plainText = String.format("a=%d&b=%s&k=%s&t=%d&e=%d&r=%d", appId, "tencentyun", secretId, now,
                                         now + expired, rdm);
        byte[] hmacDigest = hmacSha1(plainText, secretKey);
        byte[] signContent = new byte[hmacDigest.length + plainText.getBytes().length];
        System.arraycopy(hmacDigest, 0, signContent, 0, hmacDigest.length);
        System.arraycopy(plainText.getBytes(), 0, signContent, hmacDigest.length, plainText.getBytes().length);
        return base64Encode(signContent);
    }


    /**
     * 生成 base64 编码
     *
     * @param binaryData binaryData
     * @return 结果
     */
    private static String base64Encode(byte[] binaryData) {
        return new String(Base64.encodeBase64(binaryData));
    }

    /**
     * 生成 hmacsha1 签名
     *
     * @param binaryData binaryData
     * @param key        key
     * @return 结果
     * @throws Exception 异常
     */
    private static byte[] hmacSha1(byte[] binaryData, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        mac.init(secretKey);
        return mac.doFinal(binaryData);
    }

    /**
     * 生成 hmacsha1 签名
     *
     * @param plainText 文本
     * @param key       key
     * @return 结果
     * @throws Exception 异常
     */
    private static byte[] hmacSha1(String plainText, String key) throws Exception {
        return hmacSha1(plainText.getBytes(), key);
    }

    public static String aesCbcDecode(byte[] decodedText, byte[] key, byte[] ivParameter) {
        // 如果密钥不足16位，那么就补足.
        int base = 16;
        if (ivParameter.length % base != 0) {
            int groups = ivParameter.length / base + (ivParameter.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(ivParameter, 0, temp, 0, ivParameter.length);
            ivParameter = temp;
        }
        SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(ivParameter);

        try {
            Cipher cipher = Cipher.getInstance(CBC_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
            return new String(cipher.doFinal(decodedText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

}
