package com.yitu.cpcFounding.api.po;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * 时间持久对象
 *
 * @author qixinyi
 * @version 1.0
 * @date 2021/6/9 16:17
 */

@Data
public class DatePo {
    private String beginDate;
    private String endDate;
}
