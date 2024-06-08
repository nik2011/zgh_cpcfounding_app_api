package com.yitu.cpcFounding.api.utils;

import com.alibaba.fastjson.JSONObject;
import com.yitu.cpcFounding.api.po.DatePo;
import com.yitu.cpcFounding.api.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.List;

/**
 * 排行榜工具类
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/9 16:21
 */

@Slf4j
@Component
public class WeekRankingUtil {
    @Autowired
    private ConfigService configService;

    /**
     * 获取周时间
     *
     * @param
     * @return com.yitu.cpcFounding.api.po.DatePo
     * @author qixinyi
     * @date 2021/6/9 16:27
     */
    public DatePo getWeekDate() {
        List<String> list = configService.findTimes();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (list != null && list.size() > 0) {
            for (String time : list) {
                JSONObject jsonObject = JSONObject.parseObject(time);
                try {
                    // 处理数据
                    Date b = simpleDateFormat.parse(jsonObject.get("beginDate").toString());
                    Date e = simpleDateFormat.parse(jsonObject.get("endDate").toString());
                    long beginDate = DateTimeUtil.string2Millis(jsonObject.get("beginDate").toString(), "yyyy-MM-dd HH:mm:ss");
                    long endDate = DateTimeUtil.string2Millis(jsonObject.get("endDate").toString(), "yyyy-MM-dd HH:mm:ss");
                    long now = System.currentTimeMillis();

                    if (now >= beginDate && now <= endDate) {
                        DatePo datePo = new DatePo();
                        datePo.setBeginDate(DateTimeUtil.format(b, "yyyy-MM-dd HH:mm:ss"));
                        datePo.setEndDate(DateTimeUtil.format(e, "yyyy-MM-dd HH:mm:ss"));
                        return datePo;
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                    log.info(e.getMessage());
                }
            }
        }

        return null;
    }

    /**
     * 取最后一周活动时间
     *
     * @param
     * @return com.yitu.cpcFounding.api.po.DatePo
     * @author qixinyi
     * @date 2021/6/28 11:47
     */
    public DatePo getLastWeek() {
        List<String> list = configService.findTimes();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (list != null && list.size() > 0) {
            String time = list.get(list.size() - 1);

            JSONObject jsonObject = JSONObject.parseObject(time);
            try {
                // 处理数据
                Date b = simpleDateFormat.parse(jsonObject.get("beginDate").toString());
                Date e = simpleDateFormat.parse(jsonObject.get("endDate").toString());
                long beginDate = DateTimeUtil.string2Millis(jsonObject.get("beginDate").toString(), "yyyy-MM-dd HH:mm:ss");
                long endDate = DateTimeUtil.string2Millis(jsonObject.get("endDate").toString(), "yyyy-MM-dd HH:mm:ss");
                DatePo datePo = new DatePo();
                datePo.setBeginDate(DateTimeUtil.format(b, "yyyy-MM-dd HH:mm:ss"));
                datePo.setEndDate(DateTimeUtil.format(e, "yyyy-MM-dd HH:mm:ss"));
                return datePo;
            } catch (ParseException e) {
                e.printStackTrace();
                log.info(e.getMessage());
            }
        }

        return null;
    }
}
