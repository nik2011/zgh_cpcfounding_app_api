package com.yitu.cpcFounding.api.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 * @author shenjun
 * @date 2021/1/28 16:06
 */
public class TimeUtil {
    /**
     * 获取今天还剩余秒数
     * @param
     * @author shenjun
     * @date 2021/1/28 16:07
     * @return int
     */
    public static int getSeconds(){
        Calendar curDate = Calendar.getInstance();
        Calendar tommorowDate = new GregorianCalendar(curDate
                .get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate
                .get(Calendar.DATE) + 1, 0, 0, 0);
        return (int)(tommorowDate.getTimeInMillis() - curDate .getTimeInMillis()) / 1000;
    }


}
