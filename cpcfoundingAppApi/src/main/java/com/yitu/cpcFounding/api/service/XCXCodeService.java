package com.yitu.cpcFounding.api.service;

import javax.servlet.http.HttpServletRequest;

import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.ad.WXCodeVO;

/**
 * 获取小程序码
 *
 * @author clj
 * @date 2021-01-25 07:32:21
 */
public interface XCXCodeService {
    /**
     * 获取小程序码以外的相关信息
     *
     * @param request
     * @param type     类型
     * @param param    参数
     * @param flag     是否传用户id
     * @param detailId 详情id
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.ad.WXCodeVO>
     * @author qixinyi
     * @date 2021/6/18 15:40
     */
    public JsonResult<WXCodeVO> getWXCodeInfo(HttpServletRequest request, Integer type, String param, Boolean flag, Long detailId);

    /**
     * 获取小程序码
     *
     * @param param    参数
     * @param param    携带参数
     * @param flag     是否传用户id
     * @param detailId 详情id
     * @return com.yitu.partyday.api.vo.JsonResult<java.lang.Object>
     * @author qixinyi
     * @date 2021/6/9 9:17
     */
    JsonResult<Object> getCode(String param, String page, Boolean flag, Long detailId);
}
