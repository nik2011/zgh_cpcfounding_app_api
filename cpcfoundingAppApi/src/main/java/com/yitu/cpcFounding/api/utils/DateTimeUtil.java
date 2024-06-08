package com.yitu.cpcFounding.api.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间处理工具类V1
 *
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/11
 */
public final class DateTimeUtil {

    private static Logger log = LoggerFactory.getLogger(DateTimeUtil.class);
    public static final String YYYYMMDD = "yyyyMMdd";

    /**
     * 将时间对象转成字符串
     *
     * @param date    时间
     * @param pattern 格式
     * @return java.lang.String 格式化后的时间串
     * @author yanghaoming 2018/10/23 16:02
     */
    public static String format(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        if (StringUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    /**
     * 将时间戳转为时间字符串
     *
     * @param millis  毫秒值
     * @param pattern 格式
     * @return java.lang.String 格式化后的时间串
     * @author yanghaoming 2018/10/23 16:03
     */
    public static String millis2String(long millis, String pattern) {
        return format(new Date(millis), pattern);
    }

    /**
     * 将字符串转化为时间对象
     *
     * @param dateStr 时间字符串
     * @param pattern 格式
     * @return java.utils.Date 转化后的时间对象
     * @author yanghaoming 2018/10/23 16:03
     */
    public static Date parse(String dateStr, String pattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = null;
        if (StringUtil.isEmpty(dateStr)) {
            return date;
        }
        try {
            date = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }
        return date;
    }

    /**
     * 将时间字符串转为时间戳
     *
     * @param dateStr 时间字符串
     * @param pattern 格式
     * @return long 时间戳
     * @author yanghaoming 2018/10/23 16:05
     */
    public static long string2Millis(String dateStr, String pattern) {
        Date d = parse(dateStr, pattern);
        return d != null ? d.getTime() : 0;
    }

    /**
     * 获取两个时间差（毫秒）
     *
     * @param date1 时间1
     * @param date2 时间2
     * @return long 时间差（毫秒）
     * @author yanghaoming 2018/10/23 16:08
     */
    public static long getTimeSpan(Date date1, Date date2) {
        return date1.getTime() - date2.getTime();
    }

    /**
     * 计算某个时间距离当前时间的天数、小时数以及分钟数(如：1天2小时3分钟前).
     *
     * @param date 时间
     * @return java.lang.String 距离描述
     * @author yanghaoming 2018/10/23 17:04
     */
    public static String distanceTime(Date date) {
        if (date == null) {
            return "";
        }
        StringBuilder tips = new StringBuilder();
        Date now = new Date();
        long timestamp = date.getTime() - now.getTime();
        String beforeOrAfter = timestamp > 0 ? "后" : "前";
        timestamp = Math.abs(timestamp);
        long days = (timestamp / (1000 * 60 * 60 * 24));
        if (days > 0) {
            tips.append(days).append("天");
        }
        long hours = (timestamp / (1000 * 60 * 60) - days * 24);
        if (hours > 0 || (days > 0 && hours == 0)) {
            tips.append(hours).append("小时");
        }
        long minutes = (timestamp / (1000 * 60)) - days * 24 * 60 - hours * 60;
        return tips.append(minutes).append("分钟").append(beforeOrAfter).toString();
    }

    /**
     * 计算某个时间到当前时间的距离(如：1年前、1个月前、1天前、1小时前、1分钟前、刚刚)
     *
     * @param date 时间
     * @return java.lang.String 距离描述
     * @author yanghaoming 2018/10/23 16:35
     */
    public static String distanceFromNow(Date date) {
        StringBuilder tips = new StringBuilder();
        Date now = new Date();
        long timestamp = date.getTime() - now.getTime();
        String beforeOrAfter = timestamp > 0 ? "后" : "前";
        timestamp = Math.abs(timestamp);
        int year = (int) (timestamp / (1000 * 60 * 60 * 24) / 365);
        if (year > 0) {
            tips.append(year).append("年").append(beforeOrAfter);
            return tips.toString();
        }
        int month = (int) (timestamp / (1000 * 60 * 60 * 24) / 30);
        if (month > 0) {
            tips.append(month).append("个月").append(beforeOrAfter);
            return tips.toString();
        }
        int days = (int) (timestamp / (1000 * 60 * 60 * 24));
        if (days > 0) {
            tips.append(days).append("天").append(beforeOrAfter);
            return tips.toString();
        }
        int hours = (int) (timestamp / (1000 * 60 * 60));
        if (hours > 0) {
            tips.append(hours).append("小时").append(beforeOrAfter);
            return tips.toString();
        }
        int minutes = (int) (timestamp / (1000 * 60));
        if (minutes > 0) {
            tips.append(minutes).append("分钟").append(beforeOrAfter);
            return tips.toString();
        }
        return "刚刚";
    }

    /**
     * 将时间戳换成中文描述
     *
     * @param timeStamp 时间戳
     * @return java.lang.String
     * @author yaoyanhua
     * @date 2020/6/23 18:37
     */
    public static String timeStampDesc(long timeStamp) {
        timeStamp = Math.abs(timeStamp);
        StringBuilder tips = new StringBuilder("");
        long days = (timeStamp / (1000 * 60 * 60 * 24));
        if (days > 0) {
            tips.append(days).append("天");
        }
        long hours = (timeStamp / (1000 * 60 * 60) - days * 24);
        if (hours > 0) {
            tips.append(hours).append("小时");
        }
        long minutes = (timeStamp / (1000 * 60)) - days * 24 * 60 - hours * 60;
        if (minutes > 0) {
            tips.append(minutes).append("分钟");
        }
        long seconds = (timeStamp / 1000) - days * 24 * 60 * 60 - hours * 60 * 60 - minutes * 60;
        return tips.append(seconds).append("秒").toString();
    }

    /**
     * 格式化星期几
     *
     * @param week 周
     * @return java.lang.String
     * @author yaoyanhua
     * @date 2020/6/23 18:38
     */
    public static String formatWeekName(int week) {
        switch (week) {
            case Calendar.MONDAY:
                return "周一";
            case Calendar.TUESDAY:
                return "周二";
            case Calendar.WEDNESDAY:
                return "周三";
            case Calendar.THURSDAY:
                return "周四";
            case Calendar.FRIDAY:
                return "周五";
            case Calendar.SATURDAY:
                return "周六";
            case Calendar.SUNDAY:
                return "周日";
        }
        return "";
    }


    /**
     * 获取当前日期的星期一日期
     *
     * @param
     * @return java.util.Date
     * @author yaoyanhua
     * @date 2020/6/23 18:38
     */
    public static Date getCurrentMonday() {
        Date now = new Date();
        final Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        int week = cal.get(Calendar.DAY_OF_WEEK);
        Date monday = null; //周一
        int diffMon = week - Calendar.MONDAY;
        if (diffMon >= 0) {
            monday = DateUtils.addDays(now, -diffMon);
        } else {
            monday = DateUtils.addDays(now, -6);
        }

        return monday;
    }

    /**
     * 获得本周一0点时间
     *
     * @param
     * @return java.util.Date
     * @author qixinyi
     * @date 2021/6/8 17:18
     */
    public static Date getTimesWeekmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    /**
     * 获得本周日24点时间
     *
     * @param
     * @return java.util.Date
     * @author qixinyi
     * @date 2021/6/8 17:18
     */
    public static Date getTimesWeeknight() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimesWeekmorning());
        cal.add(Calendar.DAY_OF_WEEK, 7);
        return cal.getTime();
    }

    /**
     * 获取时间的"上午"\"下午"文字
     *
     * @param time 时间
     * @return java.lang.String
     * @author yaoyanhua
     * @date 2020/6/23 18:38
     */
    public static String getAmPmStr(Date time) {
        if (time == null) {
            return "";
        }
        long hour = DateUtils.getFragmentInHours(time, Calendar.DATE);
        if (hour > 12) {
            return "下午";
        }
        return "上午";
    }


    /**
     * 一天之内显示几分钟、几小时前，超过一天显示年月日
     *
     * @param date 日期
     * @return java.lang.String
     * @author yaoyanhua
     * @date 2020/6/23 18:38
     */
    public static String distanceTimeLimitOneDay(Date date) {
        if (date == null) {
            return "";
        }
        StringBuilder tips = new StringBuilder();
        Date now = new Date();
        long timestamp = date.getTime() - now.getTime();
        String beforeOrAfter = timestamp > 0 ? "后" : "前";
        timestamp = Math.abs(timestamp);
        long days = (timestamp / (1000 * 60 * 60 * 24));
        if (days > 0) {
            return tips.append(format(date, "yyyy-MM-dd")).toString();
        }
        long hours = (timestamp / (1000 * 60 * 60) - days * 24);
        if (hours > 0 || (days > 0 && hours == 0)) {
            tips.append(hours).append("小时");
        }
        long minutes = (timestamp / (1000 * 60)) - days * 24 * 60 - hours * 60;
        return tips.append(minutes).append("分钟").append(beforeOrAfter).toString();
    }

    /**
     * 校验时间有效性
     * <pre>
     *
     *                   startDate 2019-01-11
     *                   endDate 2019-01-10
     *                   month 6
     *                   return false
     *
     *                   startDate 2019-01-11
     *                   endDate 2019-07-11
     *                   month 6
     *                   return true
     *
     *                   startDate 2019-01-11
     *                   endDate 2019-04-11
     *                   month 6
     *                   return true
     *
     *                   startDate 2019-01-11
     *                   endDate 2019-07-12
     *                   month 6
     *                   return false
     *
     * </pre>
     *
     * @param startDate 2019-01-11
     * @param endDate   2019-06-02
     * @param month     6 校验时长 单位月
     * @return boolean true 在时间范围内 , false 不在时间范围内
     * @author comtu
     * @date 2019-12-10 17:46:24
     */
    public static boolean verifyValidDateScopeByMonth(Date startDate, Date endDate, int month) {
        long x = endDate.getTime() - startDate.getTime();
        if (x <= 0) {
            return false;
        }
        Calendar c1 = Calendar.getInstance();
        c1.setTime(startDate);//2019-01-11
        c1.add(Calendar.MONTH, month); // 6
        Date eTime = c1.getTime();// 6个月后的时间 2019-07-01
        long t = eTime.getTime() - endDate.getTime(); // 2019-07-01 - 2019-06-02
        return t >= 0;
    }

    /**
     * 根据生日计算年龄
     *
     * @param birthDay 生日
     * @return int
     * @author shenjun
     * @date 2020/7/24 18:02
     */
    public static int getAge(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        //出生日期晚于当前时间，无法计算
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }
        //当前年份
        int yearNow = cal.get(Calendar.YEAR);
        //当前月份
        int monthNow = cal.get(Calendar.MONTH);
        //当前日期
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        //计算整岁数
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    //当前日期在生日之前，年龄减一
                    age--;
                }
            } else {
                //当前月份在生日之前，年龄减一
                age--;
            }
        }
        return age;
    }

    /**
     * @param date, days
     * @return java.util.Date
     * @Description: 日期加减天数
     * @author liuzhaowei
     * @date 2020/7/26
     */
    public static Date addDate(Date date, int days) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);
        return calendar.getTime();
    }

    /**
     * @param date, days
     * @return java.util.Date
     * @Description: 日期加减小时
     * @author liuzhaowei
     * @date 2020/7/26
     */
    public static Date addHours(Date date, int hours) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.HOUR, hours);
        return calendar.getTime();
    }

    /**
     * 获取间隔天数
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return int
     * @author pangshihe
     * @date 2020/7/26 5:10
     */
    public static int getDayDiffer(Date startDate, Date endDate) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        long startDateTime = dateFormat.parse(dateFormat.format(startDate)).getTime();
        long endDateTime = dateFormat.parse(dateFormat.format(endDate)).getTime();
        return (int) ((endDateTime - startDateTime) / (1000 * 3600 * 24));
    }

    /**
     * 获取到第二天0点时间差（单位为秒）
     *
     * @return long
     * @author pangshihe
     * @date 2021/1/25 17:04
     */
    public static long getDiffNextDaySeconds() {
        long current = System.currentTimeMillis();// 当前时间毫秒数
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long nextDayZero = calendar.getTimeInMillis();
        long diffSeconds = (nextDayZero - current) / 1000;
        return diffSeconds;
    }

    /**
     * 将当前时间对象转成字符串
     *
     * @param pattern 格式
     * @return java.lang.String 格式化后的时间串
     * @author yanghaoming 2018/10/23 16:02
     */
    public static String formatCurrent(String pattern) {
        Date date = new Date();
        if (StringUtils.isEmpty(pattern)) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    /**
     * @param
     * @return long
     * @Description: 获取当周还剩下多少天(不包含今天
     * @author liuzhaowei
     * @date 2021/6/17
     */
    public static long getWeekLeftDays() {
        long result = 0;
        Calendar calendar = Calendar.getInstance();
        //一周的某一天
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        //星期天
        if (day == 1) {
            result = 0;
        } else {
            result = 7 - (day - 1);
        }
        return result;
    }

    /**
     * @param
     * @return long
     * @Description: 获取到下一周还剩多少秒
     * @author liuzhaowei
     * @date 2021/6/17
     */
    public static long getNextWeekSeconds() {
        //当天
        long diffSeconds = DateTimeUtil.getDiffNextDaySeconds();
        long leftDay = DateTimeUtil.getWeekLeftDays();
        diffSeconds = diffSeconds + leftDay * 24 * 60 * 60;
        return diffSeconds;
    }

}
