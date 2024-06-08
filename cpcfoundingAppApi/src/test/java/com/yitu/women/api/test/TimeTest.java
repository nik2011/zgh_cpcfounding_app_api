package com.yitu.women.api.test;

import com.yitu.cpcFounding.api.po.DatePo;
import com.yitu.cpcFounding.api.utils.DateTimeUtil;
import com.yitu.cpcFounding.api.utils.WeekRankingUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * TODO
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/8 17:07
 */

public class TimeTest extends FatherTest{
    @Autowired
    private WeekRankingUtil weekRankingUtil;

    @Test
    public void Time() throws ParseException {
        DatePo lastWeek = weekRankingUtil.getLastWeek();
        System.out.println(lastWeek.getBeginDate());
        System.out.println(lastWeek.getEndDate());

//        System.out.println("------------------------------------------------------------------------------");
//        DatePo weekDate = weekRankingUtil.getWeekDate();
//        System.out.println(weekDate.getBeginDate());
//        System.out.println(weekDate.getEndDate());
    }
}
