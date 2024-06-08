package com.yitu.cpcFounding.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yitu.cpcFounding.api.vo.ad.IndexLsAdVO;
import com.yitu.cpcFounding.api.domain.Ad;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.ad.ActiveRuleInfoVO;
import com.yitu.cpcFounding.api.vo.ad.LsAdVO;
import com.yitu.cpcFounding.api.vo.ad.SunActiveInfoVO;

import java.util.List;

/**
 * 广告服务接口层
 *
 * @author jxc
 * @date 2021/1/21
 */
public interface AdService extends IService<Ad> {
    /**
     * 首页广告信息
     * @param
     * @return ModuleDataListVO
     * @author jxc
     * @date 2021/1/21 11:40
     */
    IndexLsAdVO getIndexAdInfo();

    /**
     * 获取晒年味头图广告+获取用户数量+点赞数量+标签
     * @param
     * @return SunActiveInfoVO
     * @author jxc
     * @date 2021/1/21 14:44
     */
    //SunActiveInfoVO getSunActiveInfo();

    /**
     * 获取活动规则
     * @param
     * @return ActiveRuleVO
     * @author jxc
     * @date 2021/1/21 15:41
     */
//    ActiveRuleVO getActiveRule(Integer type);

    /**
     * @description: getActiveRuleV2
     * @param type
     * @return: ActiveRuleVO
     * @author: luzhichao
     * @date: 2021/1/29 9:52
     */
    JsonResult<List<ActiveRuleInfoVO>> getActiveRuleV2(Integer type);

    /**
     * @description: 获取广告
     * @param type
     * @return: JsonResult<java.util.List<LsAdVO>>
     * @author: luzhichao
     * @date: 2021/1/29 16:36
     */
    JsonResult<List<LsAdVO>> getLsAd(Integer type);

    /**
     * 摇一摇首页广告
     *
     * @return LsAdVO
     * @author qinmingtong
     * @date 2021/1/27 14:58
     */
    //LsAdVO lsAdOfYaoYiYao();

    /**
     * 根据广告类型取出广告列表
     * @author zhangyongfeng
     * @param type
     * @return java.util.List<LsAd>
     * @date 2021/1/27 15:53
     */
    List<Ad> getLsAdListByType(int type);

}
