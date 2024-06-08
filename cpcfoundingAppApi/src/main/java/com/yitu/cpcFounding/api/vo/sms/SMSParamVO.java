package com.yitu.cpcFounding.api.vo.sms;

import lombok.Data;

/**
 * LsYearAdminWebSystem
 * 2021/1/25 17:34
 * 短信参数
 *
 * @author luzhichao
 **/
@Data
public class SMSParamVO {

    private String cmd = "send";

    /** 账号 */
    private String uid;

    /** 密码 */
    private String psw;

    /** 手机号 */
    private String mobiles;

    /** 消息编号 */
    private String msgid;

    /** 消息内容 */
    private String msg;

}
