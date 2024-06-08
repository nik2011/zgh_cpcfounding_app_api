package com.yitu.cpcFounding.api.vo.sms;

import lombok.Data;

import java.util.Date;

/**
 * 创建人
 * LsYearApiSystem
 * 2021/1/26 10:32
 * 短信发送验证记录信息
 *
 * @author luzhichao
 **/
@Data
public class SMSSendStatusVO {

    /** 用户id */
    private Integer userId;

    /** 手机号 */
    private String phone;

    /** 最近一次发送时间 */
    private Date lastTime;

    /** 发送次数 */
    private int count;

    /** 验证码 */
    private String code;
}
