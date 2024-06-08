package com.yitu.cpcFounding.api.controller.ad;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yitu.cpcFounding.api.service.AdService;
import com.yitu.cpcFounding.api.vo.ad.IndexLsAdVO;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.ad.ActiveRuleInfoVO;
import com.yitu.cpcFounding.api.vo.ad.LsAdVO;
import com.yitu.cpcFounding.api.vo.ad.SunActiveInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 广告表控制器
 * @author shenjun
 * @date 2021-01-21 11:02:18
 */
@RestController
@Api(value = "首页广告信息接口", tags = "首页广告信息接口")
public class AdController {

    @Resource
    private AdService lsadService;

    /**
     * 获取首页广告信息
     * @param
     * @return JsonResult<ModuleDataListVO>
     * @author jxc
     * @date 2021/1/21 11:37
     */
    @PostMapping("/index/ad/info")
    @ApiOperation(value = "首页广告信息")
    @ApiOperationSupport(author = "江学成")
    public JsonResult<IndexLsAdVO> getIndexAdInfo(){
        return JsonResult.ok(lsadService.getIndexAdInfo());
    }

    /**
     *  获取晒年味头图广告+获取用户数量+点赞数量+标签
     * @param
     * @return JsonResult<T>
     * @author jxc
     * @date 2021/1/21 14:30
     */
    /*@PostMapping("/sun/active/info")
    @ApiOperation(value = "获取晒年味头图广告+获取用户数量+点赞数量+标签")
    @ApiOperationSupport(author = "江学成")
    public JsonResult<SunActiveInfoVO> getSunActiveInfo(){
        return JsonResult.ok(lsadService.getSunActiveInfo());
    }*/

//    /**
//     * 获取活动规则
//     * @param
//     * @return JsonResult<ActiveRuleVO>
//     * @author jxc
//     * @date 2021/1/21 15:40
//     */
//    @Deprecated
//    @PostMapping("/active/rule")
//    @ApiOperation(value = "获取晒年味活动规则")
//    public JsonResult<ActiveRuleVO> getActiveRule(@ApiParam(value = "类型：1晒年味规则") @RequestParam int type){
//        return JsonResult.ok(lsadService.getActiveRule(type));
//    }

    /**
     * @description: 获取活动规则
     * @param type
     * @return: JsonResult<java.util.List<ActiveRuleInfoVO>>
     * @author: luzhichao
     * @date: 2021/1/29 10:27
     */
    @PostMapping("/active/ruleV2")
    @ApiOperation(value = "获取活动规则")
    @ApiOperationSupport(author = "luzhichao")
    public JsonResult<List<ActiveRuleInfoVO>> getActiveRuleV2(@ApiParam(value = "类型：14晒活动规则，15暖城活动规则，16摇一摇活动规则，17奖品详情，18活动声明，19摇一摇广告，20隐私政策,35 送奖章活动规则,36 大转盘抽奖活动规则", required = true)
                                                        @RequestParam int type){
        return lsadService.getActiveRuleV2(type);
    }

    /**
     * @description: 获取广告
     * @param type
     * @return: JsonResult<java.util.List<ActiveRuleInfoVO>>
     * @author: luzhichao
     * @date: 2021/1/29 10:27
     */
    @PostMapping("/active/lsAd")
    @ApiOperation(value = "获取广告")
    @ApiOperationSupport(author = "luzhichao")
    public JsonResult<List<LsAdVO>> getLsAd(@ApiParam(value = "类型：广告类型：1banner图，2福利活动，3视屏广告，4惠服务，5深圳工会，6暖城，7首页顶部图,8暖城分享,9摇一摇分享,10晒的分享,11晒头图,12暖城头图,13摇一摇头图，14晒活动规则，15暖城活动规则，16摇一摇活动规则，17奖品详情，18活动声明，19摇一摇广告，20隐私政策", required = true)
                                                              @RequestParam int type){
        return lsadService.getLsAd(type);
    }

    /**
     * 摇一摇首页广告
     *
     * @return JsonResult<LsAdVO>
     * @author qinmingtong
     * @date 2021/1/27 14:57
     */
   /* @PostMapping("/active/lsAdOfYaoYiYao")
    @ApiOperation(value = "摇一摇首页广告", notes = "摇一摇首页广告")
    @ApiOperationSupport(author = "秦明通")
    public JsonResult<LsAdVO> lsAdOfYaoYiYao(){
        return JsonResult.ok(lsadService.lsAdOfYaoYiYao());
    }*/

}
