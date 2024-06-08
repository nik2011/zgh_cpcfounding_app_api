package com.yitu.cpcFounding.api.service;

import com.yitu.cpcFounding.api.vo.JsonResult;

/**
 * 微信代金券service
 * @author chenpinjia
 * @version 1.0
 * @date 2021/1/23
 */
public interface WxVoucherService {

    /**
     * 发放代金券
     * @param id 用户奖品记录id
     * @return void
     * @author chenpinjia
     * @date 2021/1/26 20:24
     */
    JsonResult sendVoucher(Long id);


}
