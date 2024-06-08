package com.yitu.cpcFounding.api.service;

import com.yitu.cpcFounding.api.vo.JsonResult;

/**
 * 创建人
 * LsYearAdminWebSystem
 * 2021/1/25 18:12
 *
 * @author luzhichao
 **/
public interface SMSSevice {

    JsonResult sendVerifiCode(String phone);

    /**
     * @description: 验证码校验
     * @param phone
     * @param code
     * @return: boolean
     * @author: luzhichao
     * @date: 2021/1/26 11:47
     */
    JsonResult<String> verifiCode(String phone, String code);

    JsonResult sendMsg(String content, String phone);

}
