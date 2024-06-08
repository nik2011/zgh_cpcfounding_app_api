package com.yitu.cpcFounding.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yitu.cpcFounding.api.domain.ShareRecord;
import com.yitu.cpcFounding.api.vo.JsonResult;

/**
 * @Auther: zhangyongfeng
 * @Date: 2021/1/22 11 : 45
 * @Description:
 */
public interface ShareRecordService extends IService<ShareRecord> {

    /**
     * 添加用户分享记录信息
     *
     * @param type   1.摇一摇活动 2.晒年味 3.暖城活动
     * @return JsonResult
     * @author zhangyongfeng
     * @date 2021/1/22 11:51
     */
    JsonResult addShareRecord(Integer type);

}
