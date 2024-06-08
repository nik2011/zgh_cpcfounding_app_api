package com.yitu.cpcFounding.api.service;

import com.yitu.cpcFounding.api.vo.AuthSuccessDetailVo;
import com.yitu.cpcFounding.api.vo.JsonResult;

/**
 * 工会认证服务接口层
 *
 * @author jxc
 * @date 2021/1/23
 */
public interface TradeUnionService {

    /**
     * 工会认证
     *
     * @param phone
     * @return JsonResult<java.lang.Boolean>
     * @author shenjun
     * @date 2021-4-8 18:08:45
     */
    JsonResult<Boolean> tradeUnionAuth(String phone);

    /**
     * 工会认证
     * @param phone
     * @return JsonResult<java.lang.Boolean>
     * @author jxc
     * @date 2021/1/23 10:48
     */
    JsonResult<Boolean> tradeUnionAuth(String phone,String code);

    /**
     * 直接认证工会会员
     * @param phone
     * @return
     */
    JsonResult<Boolean> requestAuth(String phone);

    /**
     * 认证成功详情
     * @param
     * @return JsonResult<AuthSuccessDetailVo>
     * @author jxc
     * @date 2021/1/26 11:18
     */
    JsonResult<AuthSuccessDetailVo> getAuthSuccessDetail();
}
