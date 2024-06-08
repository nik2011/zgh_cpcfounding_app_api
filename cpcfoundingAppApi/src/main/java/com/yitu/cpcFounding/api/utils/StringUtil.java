package com.yitu.cpcFounding.api.utils;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * String工具类
 *
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/11
 */
public class StringUtil {

    private static Logger log = LoggerFactory.getLogger(StringUtil.class);

    private static final String MASK_STR = "*";

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return boolean
     * @author yaoyanhua
     * @date 2020/6/23 17:59
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }
        return false;
    }

    /**
     * 转long
     *
     * @param str 字符串
     * @return long
     * @author yaoyanhua
     * @date 2020/6/23 17:59
     */
    public static long stringToLong(String str) {
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    /**
     * 转int
     *
     * @param str 字符串
     * @return int
     * @author yaoyanhua
     * @date 2020/6/23 18:00
     */
    public static int stringToInteger(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    /**
     * 字符串转int数组
     *
     * @param str 字符串
     * @return java.util.List<java.lang.Integer>
     * @author yaoyanhua
     * @date 2020/6/23 18:00
     */
    public static List<Integer> stringToList(String str) {
        List<Integer> list = new ArrayList<>(0);
        if (StringUtils.isBlank(str)) {
            return list;
        }
        try {
            String[] strArr = str.split(",");
            for (String item : strArr) {
                list.add(Integer.parseInt(item));
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    /**
     * 字符串转int数组
     *
     * @param str 字符串
     * @return java.util.List<java.lang.Integer>
     * @author yaoyanhua
     * @date 2020/6/23 18:00
     */
    public static List<Long> stringToLongList(String str) {
        List<Long> list = new ArrayList<>(0);
        if (StringUtils.isBlank(str)) {
            return list;
        }
        try {
            String[] strArr = str.split(",");
            for (String item : strArr) {
                list.add(Long.valueOf(item));
            }
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        return list;
    }

    /**
     * 用户身份证号码的打码隐藏加星号加*
     *
     * @return 处理完成的身份证
     */
    public static String stringMask(String val) {
        String res = "";
        if (!StringUtils.isEmpty(val)) {
            StringBuilder stringBuilder = new StringBuilder(val);
            if (val.length() >= 18) {
                res = stringBuilder.replace(4, 14, "**********").toString();
            } else if (val.length() >= 9) {
                res = stringBuilder.replace(2, 6, "****").toString();
            } else if (val.length() >= 3) {
                res = stringBuilder.replace(1, 2, "*").toString();
            } else if (val.length() >= 2) {
                res = stringBuilder.replace(1, 2, "*").toString();
            } else if (val.length() >= 1) {
                res = stringBuilder.replace(0, 1, "*").toString();
            }
        }
        return res;
    }

    /**
     * 转long
     *
     * @param str object对象
     * @return long
     * @author yaoyanhua
     * @date 2020/6/23 17:59
     */
    public static long objectToLong(Object str) {
        if (str == null) {
            return 0;
        }
        try {

            return Long.parseLong(str.toString());
        } catch (NumberFormatException e) {
            log.error(e.getMessage(), e);
        }
        return 0;
    }

    /**
     * 用户名打码加*，一个字符显示全部，两个字字符显示第一个字符后面加星号，三个以上字符显示第一个和最后一个字符，其他字符用星号代替
     *
     * @param userName 用户名
     * @return java.lang.String
     * @author qinmingtong
     * @date 2021/1/27 14:22
     */
    public static String userNameMask(String userName) {
        int size = 2;
        if (StringUtil.isEmpty(userName) || userName.length() < size) {
            return userName;
        } else {
            if (userName.length() == size) {
                return userName.substring(0, 1) + Strings.repeat(MASK_STR, 1);
            } else {
                return userName.substring(0, 1) + Strings.repeat(MASK_STR, userName.length() - 2)
                        + userName.substring(userName.length() - 1);
            }
        }
    }


    /**
     * 手机号脱敏
     * @param phoneNumber 手机号
     * @author shenjun
     * @date 2021/2/5 12:01
     * @return java.lang.String
     */
    public static String phoneMask(String phoneNumber) {
        if(StringUtils.isNotEmpty(phoneNumber)){
            phoneNumber = phoneNumber.replaceAll("(\\w{3})\\w*(\\w{4})", "$1****$2");
        }
        return phoneNumber;
    }

}
