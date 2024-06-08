package com.yitu.cpcFounding.api.utils;

import com.yitu.cpcFounding.api.constant.WatchConstant;
import com.yitu.cpcFounding.api.vo.redis.YaoPrizeBookVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 公共工具类
 *
 * @author pangshihe
 * @date 2020/6/15 10:16
 */
@Slf4j
public class CommonUtil {

    /**
     * 判断是不是Windows系统.
     *
     * @return 返回是不是Windows系统.
     */
    public static boolean isOsWindows() {
        String osname = System.getProperty("os.name").toLowerCase();
        boolean rt = osname.startsWith("windows");
        return rt;
    }

    /**
     * 将路径修正为当前操作系统所支持的形式.
     *
     * @param path 源路径.
     * @return 返回修正后的路径.
     */
    public static String fixPath(String path) {
        if (null == path) return path;
        if (path.length() >= 1 && ('/' == path.charAt(0) || '\\' == path.charAt(0))) {
            // 根目录, Windows下需补上盘符.
            if (isOsWindows()) {
                String userdir = System.getProperty("user.dir");
                if (null != userdir && userdir.length() >= 2) {
                    return userdir.substring(0, 2) + path;
                }
            }
        }
        return path;
    }

    /**
     * 字符串返回处理
     *
     * @return
     */
    public static String getStringSymbolProcessing(String s) {
        s = s.replaceAll("\'", "&prime;").replaceAll("<", "&#60;").replaceAll(">", "&#62;").replaceAll("\"", "&quot;").replaceAll("\n", "<br>\n");
        return s;
    }


    /**
     * 把特殊字符全替换成空格
     *
     * @param character
     * @return
     */
    public static String getSpecialCharacter(String character) {
        String regEx = "[`~!@#$%^&*()+=|{}<>'\";',?-]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(character);
        return m.replaceAll(" ").trim();
    }


    /**
     * 是否有特殊字符
     *
     * @param character character
     * @return boolean
     * @author chenpinjia
     * @date 2021/2/3 9:30
     */
    public static boolean containSpecialCharacter(String... character) {
        String regEx = "[`~!@#$%^&*()+=|{}<>'\";',?-]";
        Pattern p = Pattern.compile(regEx);
        for (String c : character) {
            Matcher m = p.matcher(c);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 转换文件大小
     */
    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    /**
     * @param path   原图路径
     * @param width  生成的宽度
     * @param height 高度
     * @return java.lang.String
     * @Description:获取缩略图路径
     * @author liuzhaowei
     * @date 2020/7/27
     */
    public static String getThumbPath(String path, int width, int height) {
        if (StringUtils.isBlank(path)) {
            return "";
        }
        String[] arr = path.split("\\.");
        String thumbPath = String.format("%s_%sx%s.%s", arr[0], width, height, arr[1]);
        return thumbPath;
    }

    /**
     * 验证手机号码（支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     *
     * @param phone 移动、联通、电信运营商的号码段
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean isPhone(String phone) {
        String regex = "^(\\+\\d+)?1[123456789]\\d{9}$";
        return Pattern.matches(regex, phone);
    }


    /**
     * 验证数字
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkNumber(String digit) {
        String regex = "^[0-9]+$";
        return Pattern.matches(regex, digit);
    }

    /**
     * 验证香港手机号
     *
     * @param digit 一位或多位0-9之间的整数
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkHKPhone(String digit) {
        String regex = "^[0-9]{1,10}$";
        return Pattern.matches(regex, digit);
    }

    /**
     * 验证居民省份证
     *
     * @param
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIdCard(String value) {
        String regex = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
        return Pattern.matches(regex, value);
    }

    /**
     * 验证港澳居民来往内地通行证
     *
     * @param
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkHomecomingPermit(String value) {
        //String regex = "^((\\s?[A-Za-z])|([A-Za-z]{2}))\\d{6}((\\([0-9aA]\\))|([0-9aA]))$";
        String regex = "^[HMhm](\\d{8}|\\d{10})$";
        return Pattern.matches(regex, value);
    }

    /**
     * 验证往来港澳通行证
     *
     * @param
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIDHK(String value) {
        String regex = "^[a-zA-Z0-9]{6,10}$";
        return Pattern.matches(regex, value);
    }


    /**
     * 验证台湾居民来往内地通行证
     * 2013年前是1个英文字+7位数字，于2013年改为8位数字。
     *
     * @param
     * @return 验证成功返回true，验证失败返回false
     */
    public static boolean checkIDTaiwan(String value) {
        String regex = "^[a-zA-Z]?[0-9]{7,8}$";
        return Pattern.matches(regex, value);
    }

    /**
     * 护照验证
     * 规则： G + 8位数字, P + 7位数字, S/D + 7或8位数字,等
     * 例： G12345678, P1234567
     */
    public static Boolean checkPassportCard(String card) {
        String reg = "^([a-zA-Z]|[0-9]){5,17}$";
        if (card.matches(reg) == false) {
            //护照号码不合格
            return false;
        } else {
            //校验通过
            return true;
        }
    }

    /**
     * 验证银行卡号
     * https://blog.csdn.net/iteye_9007/article/details/82641462
     * 16~19位数
     * 从卡号最后一位数字开始，逆向将奇数位相加
     * 从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，则将其减去9），再求和
     * 奇偶相加为10的倍数表示通过
     *
     * @param bankCardNo 银行卡号
     * @return boolean
     * @author chenpinjia
     * @date 2021/1/27 14:43
     */
    public static boolean checkBankCardNo(String bankCardNo) {
        //验证
        if (!bankCardNo.matches("^\\d{16,19}$")) {
            return false;
        }
        //倒转
        String reverseCode = new StringBuffer(bankCardNo).reverse().toString();
        char[] array = reverseCode.toCharArray();
        int sumOdd = 0;
        int sumEven = 0;
        for (int i = 0; i < reverseCode.length(); i++) {
            int num = Integer.parseInt(String.valueOf(array[i]));
            if (i % 2 == 0) {
                //奇数位
                sumEven += num;
            } else {
                //偶数位
                num = num * 2;
                if (num > 9) {
                    num = num - 9;
                }
                sumOdd += num;
            }
        }
        return (sumOdd + sumEven) % 10 == 0;
    }

    /**
     * @param
     * @return YaoPrizeBookVo
     * @Description: 获取未中奖奖品
     * 5积分：12%
     * 30积分：35%
     * 50积分：35%
     * 100积分：15%
     * 1000积分：3%
     * @author liuzhaowei
     * @date 2021/1/22
     */
    public static YaoPrizeBookVo getNoWinningPrize(HttpServletRequest request) {
        String[] titleArr = {"积分5", "积分30", "积分50", "积分100"};
        String[] valueArr = {"5", "30", "50", "100"};
        String[] imageArr = {"/images/nowining0.png", "/images/nowining0.png", "/images/nowining0.png",
                "/images/nowining0.png"};
        int random = RandomUtils.nextInt(0, 101);
        int index = 0;
        if (random <= 12) {
            index = 0;
        } else if (random <= 47) {
            index = 1;
        } else if (random <= 82) {
            index = 2;
        } else if (random <= 100) {
            index = 3;
        }
        YaoPrizeBookVo noYaoPrizeBookVo = new YaoPrizeBookVo();
        noYaoPrizeBookVo.setId(0);
        noYaoPrizeBookVo.setImageUrl(HttpUtil.getWebResourcePath(imageArr[index]));
        noYaoPrizeBookVo.setTitle(titleArr[index]);
        noYaoPrizeBookVo.setPrizeValue(Integer.valueOf(valueArr[index]));
        return noYaoPrizeBookVo;
    }


    /***
     * @description: 扩大点赞数
     * @param praiseNum 实际点赞数
     * @return: java.lang.Long
     * @author: luzhichao
     * @date: 2021/1/28 17:44
     */
    public static Long enlargePraiseNum(long praiseNum) {
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(praiseNum));
        BigDecimal bigDecimal2 = new BigDecimal(String.valueOf(WatchConstant.CARDINAL_NUMBER));
        return bigDecimal1.multiply(bigDecimal2).longValue();
    }

    /**
     * 转换文件大小
     */
    public static String formatNumber(long num) {
        DecimalFormat df = new DecimalFormat("#.0");
        String numStr = "";
        if (num < 1000) {
            return String.valueOf(num);
        }
        if (num < 10000) {
            numStr = df.format((double) num / 1000) + "k";
        } else {
            numStr = df.format((double) num / 10000) + "w";
        }
        return numStr;
    }

    /**
     * 送奖章随机数字
     *
     * @return java.lang.String
     * @author wangping
     * @date 2021/4/9 18:00
     */
    public static int medalRand() {
        int[] meaalArr = {51, 66, 88, 99, 666, 999, 1234};
        int random = RandomUtils.nextInt(0, meaalArr.length);
        return meaalArr[random];
    }

}
