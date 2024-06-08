package com.yitu.cpcFounding.api.vo;

import lombok.Data;

import java.util.Date;

/**
 * 活动开始和结束时间
 *
 * @author pangshihe
 * @date 2021/1/29
 */
@Data
public class ActivityStartAndEndDateVO {
    private Date beginDate;
    private Date endDate;
}
