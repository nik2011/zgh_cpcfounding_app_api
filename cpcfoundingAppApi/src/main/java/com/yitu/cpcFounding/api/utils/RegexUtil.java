package com.yitu.cpcFounding.api.utils;

import java.util.regex.Pattern;

/**
 * TODO
 *
 * @author jxc
 * @date 2021/1/29
 */
public class RegexUtil {
    /**
     *  正则：手机号（简单）, 1字头＋10位数字即可.
     * @param phone
     * @return
     */
    public static boolean validateMobilePhone(String phone) {
        Pattern pattern = Pattern.compile("^[1]\\d{10}$");
        return pattern.matcher(phone).matches();
    }


    public static void main(String[] args) {
        System.out.println(RegexUtil.validateMobilePhone("12345678901"));
    }

}
