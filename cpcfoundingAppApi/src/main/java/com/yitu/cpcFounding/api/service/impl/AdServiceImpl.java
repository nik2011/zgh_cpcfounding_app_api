package com.yitu.cpcFounding.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yitu.cpcFounding.api.constant.Configs;
import com.yitu.cpcFounding.api.domain.Ad;
import com.yitu.cpcFounding.api.domain.Config;
import com.yitu.cpcFounding.api.enums.ConfigEnum;
import com.yitu.cpcFounding.api.mapper.AdMapper;
import com.yitu.cpcFounding.api.redis.RedisUtil;
import com.yitu.cpcFounding.api.service.AdService;
import com.yitu.cpcFounding.api.service.ConfigService;
import com.yitu.cpcFounding.api.utils.BeanCopyUtil;
import com.yitu.cpcFounding.api.utils.HttpUtil;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.ad.*;
import com.yitu.cpcFounding.api.wrapper.HeadBackPicWrapper;
import com.yitu.cpcFounding.api.wrapper.HeadPicIsadWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 广告服务实现层
 *
 * @author jxc
 * @date 2021/1/21
 */
@Slf4j
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {

    @Resource
    private HttpServletRequest request;
    @Resource
    private ConfigService configService;

    @Autowired
    private RedisUtil redisUtil;

    //首页广告组
    private final int[] AD_TYPE={7,25,2,26,27,28,1,3,4,5};


    /**
     * 首页广告信息
     *
     * @param
     * @return ModuleDataListVO
     * @author jxc
     * @date 2021/1/21 11:40
     */
    @Override
    public IndexLsAdVO getIndexAdInfo() {
       /* List<Integer> typeList = new ArrayList<>();
        typeList.add(AdTypeEnum.TOP_PIC_INDEX.getValue());
        typeList.add(AdTypeEnum.LUCKY_DRAW.getValue());
        typeList.add(AdTypeEnum.MATERIAL_ACTIVE.getValue());
        typeList.add(AdTypeEnum.EXCLUSIVE_HEAD.getValue());
        typeList.add(AdTypeEnum.IMAGE_PHONE.getValue());
        typeList.add(AdTypeEnum.HOME_LIST_AD.getValue());
        typeList.add(AdTypeEnum.BANNER.getValue());
        typeList.add(AdTypeEnum.VIDEO.getValue());
        typeList.add(AdTypeEnum.HUI_FU_WU.getValue());
        typeList.add(AdTypeEnum.GRADE.getValue());

        List<Ad> adList = new ArrayList<>();
        typeList.forEach(type->{
            List<Ad> list = this.getLsAdListByType(type);
            if(CollectionUtils.isNotEmpty(list)){
                adList.addAll(list);
            }
        });

        adList.forEach(item->{
            if(StringUtils.isNotBlank(item.getPicPath())){
                item.setPicPath(HttpUtil.getNetResourcePath(request,item.getPicPath()));
            }
        });

        // 头部背景图
        List<LsAdVO> topPicIndex = AdWrapper.build().listVO(adList.stream()
                                                                   .filter(item -> item.getType().equals(AdTypeEnum.TOP_PIC_INDEX.getValue()))
                                                                   .sorted(Comparator.comparing(Ad::getType))
                                                                   .collect(Collectors.toList()));

        HeadBackPicVO headBackPic = null;
        if(CollectionUtils.isNotEmpty(topPicIndex)){
            headBackPic = HeadBackPicWrapper.build().entityVO(topPicIndex.get(0));
        }

        //幸运大抽奖
        List<LsAdVO> luckyDraw =  AdWrapper.build().listVO(adList.stream()
                .filter(item -> item.getType().equals(AdTypeEnum.LUCKY_DRAW.getValue()))
                .sorted(Comparator.comparing(Ad::getType))
                .collect(Collectors.toList()));

        //福利活动、圳扶贫
        List<LsAdVO> materialActive =  AdWrapper.build().listVO(adList.stream()
                .filter(item -> item.getType().equals(AdTypeEnum.MATERIAL_ACTIVE.getValue()))
                .sorted(Comparator.comparing(Ad::getType))
                .collect(Collectors.toList()));

        //专属头像
        List<LsAdVO> exclusiveHead =  AdWrapper.build().listVO(adList.stream()
                .filter(item -> item.getType().equals(AdTypeEnum.EXCLUSIVE_HEAD.getValue()))
                .sorted(Comparator.comparing(Ad::getType))
                .collect(Collectors.toList()));

        //形象照
        List<LsAdVO> imagePhone =  AdWrapper.build().listVO(adList.stream()
                .filter(item -> item.getType().equals(AdTypeEnum.IMAGE_PHONE.getValue()))
                .sorted(Comparator.comparing(Ad::getType))
                .collect(Collectors.toList()));

        //首页列表广告
        List<LsAdVO> homeListAd =  AdWrapper.build().listVO(adList.stream()
                .filter(item -> item.getType().equals(AdTypeEnum.HOME_LIST_AD.getValue()))
                .sorted(Comparator.comparing(Ad::getType))
                .collect(Collectors.toList()));

        // banner图
        List<LsAdVO> bannerList = AdWrapper.build().listVO(adList.stream()
                .filter(item -> item.getType().equals(AdTypeEnum.BANNER.getValue()))
                .sorted(Comparator.comparing(Ad::getType))
                .collect(Collectors.toList()));

        // 视频广告
        List<LsAdVO> lsAdVos3 = AdWrapper.build().listVO(adList.stream()
                .filter(item -> item.getType().equals(AdTypeEnum.VIDEO.getValue()))
                .sorted(Comparator.comparing(Ad::getType))
                .collect(Collectors.toList()));
        // 惠服务
        List<LsAdVO> lsAdVos4 = AdWrapper.build().listVO(adList.stream()
                .filter(item -> item.getType().equals(AdTypeEnum.HUI_FU_WU.getValue()))
                .sorted(Comparator.comparing(Ad::getType))
                .collect(Collectors.toList()));
        // 深圳工会
        List<LsAdVO> lsAdVos5 = AdWrapper.build().listVO(adList.stream()
                .filter(item -> item.getType().equals(AdTypeEnum.GRADE.getValue()))
                .sorted(Comparator.comparing(Ad::getType))
                .collect(Collectors.toList()));*/
        IndexLsAdVO indexLsAdVo = new IndexLsAdVO();
        List<ModuleDataListVO> moduleDataList = new ArrayList<>();

       for(int type:AD_TYPE){
           Config config=configService.getConfigByTypeAndKey(ConfigEnum.AD_TYPE_LABEL.getCode(),String.valueOf(type));
           if(config !=null){
               ModuleDataListVO luckyDrawVO = new ModuleDataListVO();
               List<Ad> list = this.getLsAdListByType(type);
               if(CollectionUtils.isNotEmpty(list)){

                   if(type == 7){//头部背景图
                       List<LsAdVO> lsAdVOList = list.stream().map(o ->
                               BeanCopyUtil.of(o, new LsAdVO()).copy(BeanUtils::copyProperties).get()
                       ).collect(Collectors.toList());
                       HeadBackPicVO  headBackPic= HeadBackPicWrapper.build().entityVO(lsAdVOList.get(0));
                       indexLsAdVo.setHeadBackPic(headBackPic);
                   }else {
                       luckyDrawVO.setType(type);
                       luckyDrawVO.setTitle(config.getKeyValue());

                       List<LsAdVO> lsAdVOList = list.stream().map(o ->
                               BeanCopyUtil.of(o, new LsAdVO()).copy(BeanUtils::copyProperties).get()
                       ).collect(Collectors.toList());
                       luckyDrawVO.setItems(lsAdVOList);
                       moduleDataList.add(luckyDrawVO);
                   }
               }
           }

       }
        indexLsAdVo.setModuleDataLists(moduleDataList);

        /*List<ModuleDataListVO> moduleDataList = new ArrayList<>();

        //幸运大抽奖
        ModuleDataListVO luckyDrawVO = new ModuleDataListVO();
        luckyDrawVO.setType(AdTypeEnum.LUCKY_DRAW.getValue());
        luckyDrawVO.setTitle(AdTypeEnum.LUCKY_DRAW.getDesc());
        luckyDrawVO.setItems(luckyDraw);
        moduleDataList.add(luckyDrawVO);

        //福利活动、圳扶贫
        ModuleDataListVO materialActiveVO = new ModuleDataListVO();
        materialActiveVO.setType(AdTypeEnum.MATERIAL_ACTIVE.getValue());
        materialActiveVO.setTitle(AdTypeEnum.MATERIAL_ACTIVE.getDesc());
        materialActiveVO.setItems(materialActive);
        moduleDataList.add(materialActiveVO);

        //专属头像
        ModuleDataListVO exclusiveHeadVO = new ModuleDataListVO();
        exclusiveHeadVO.setType(AdTypeEnum.EXCLUSIVE_HEAD.getValue());
        exclusiveHeadVO.setTitle(AdTypeEnum.EXCLUSIVE_HEAD.getDesc());
        exclusiveHeadVO.setItems(exclusiveHead);
        moduleDataList.add(exclusiveHeadVO);

        //形象照
        ModuleDataListVO imagePhoneVO = new ModuleDataListVO();
        imagePhoneVO.setType(AdTypeEnum.IMAGE_PHONE.getValue());
        imagePhoneVO.setTitle(AdTypeEnum.IMAGE_PHONE.getDesc());
        imagePhoneVO.setItems(imagePhone);
        moduleDataList.add(imagePhoneVO);

        //首页列表广告
        ModuleDataListVO homeListAdVO = new ModuleDataListVO();
        homeListAdVO.setType(AdTypeEnum.HOME_LIST_AD.getValue());
        homeListAdVO.setTitle(AdTypeEnum.HOME_LIST_AD.getDesc());
        homeListAdVO.setItems(homeListAd);
        moduleDataList.add(homeListAdVO);

        // banner图
        ModuleDataListVO bannerListVO = new ModuleDataListVO();
        bannerListVO.setType(AdTypeEnum.BANNER.getValue());
        bannerListVO.setTitle(AdTypeEnum.BANNER.getDesc());
        bannerListVO.setItems(bannerList);
        moduleDataList.add(bannerListVO);

        // 视频广告数据组装
        ModuleDataListVO indexLsAdVo3 = new ModuleDataListVO();
        indexLsAdVo3.setType(AdTypeEnum.VIDEO.getValue());
        indexLsAdVo3.setTitle(AdTypeEnum.VIDEO.getDesc());
        indexLsAdVo3.setItems(lsAdVos3);
        moduleDataList.add(indexLsAdVo3);

        // 惠服务数据组装
        ModuleDataListVO indexLsAdVo4 = new ModuleDataListVO();
        indexLsAdVo4.setType(AdTypeEnum.HUI_FU_WU.getValue());
        indexLsAdVo4.setTitle(AdTypeEnum.HUI_FU_WU.getDesc());
        indexLsAdVo4.setItems(lsAdVos4);
        moduleDataList.add(indexLsAdVo4);

        // 深圳工会数据组装
        ModuleDataListVO indexLsAdVo5 = new ModuleDataListVO();
        indexLsAdVo5.setType(AdTypeEnum.GRADE.getValue());
        indexLsAdVo5.setTitle(AdTypeEnum.GRADE.getDesc());
        indexLsAdVo5.setItems(lsAdVos5);
        moduleDataList.add(indexLsAdVo5);

        IndexLsAdVO indexLsAdVo = new IndexLsAdVO();
        indexLsAdVo.setHeadBackPic(headBackPic);
        indexLsAdVo.setModuleDataLists(moduleDataList);*/
        return indexLsAdVo;
    }

    /**
     * 获取晒年味头图广告+获取用户数量+点赞数量+标签
     *
     * @param
     * @return SunActiveInfoVO
     * @author jxc
     * @date 2021/1/21 14:44
     */
    /*@Override
    public SunActiveInfoVO getSunActiveInfo() {
        SunActiveInfoVO sunActiveInfoVo = new SunActiveInfoVO();

        //顶部广告
        List<Ad> ads = this.getLsAdListByType(IC_YEAR_PRAISE[0]);
        HeadPicIsadVO headPicIsad = getHeadPicIsadVO(ads);
        //中间广告
        ads = this.getLsAdListByType(IC_YEAR_PRAISE[1]);
        HeadPicIsadVO middlePicIsad = getHeadPicIsadVO(ads);

        // 帖子列表标签
        //List<LsConfig> showYeatTags = configService.getConfigListByType(ConfigEnum.YEAR_FESTIVAL.getCode());
        //List<ShowYearTagVO> headTags = showYeatTags.stream().map(item->new ShowYearTagVO(item.getKeyId(),item.getKeyValue())).collect(Collectors.toList());
        // 是否显示获奖名单
        List<Config> configs = configService.getConfigListByType(ConfigEnum.SHOW_YEAR_PRIZE_WINNER.getCode());
        if(CollectionUtils.isNotEmpty(configs)){
            sunActiveInfoVo.setIsShowYearPrizeWinner(Integer.parseInt(configs.get(0).getKeyValue()));
        }else{
            sunActiveInfoVo.setIsShowYearPrizeWinner(0);
        }

        sunActiveInfoVo.setHeadPicIsad(headPicIsad);
        sunActiveInfoVo.setMiddlePicIsad(middlePicIsad);
        //sunActiveInfoVo.setHeadTags(headTags);
        //sunActiveInfoVo.setPartakeUserNum(lsShowYearService.getUserTotal());
        //sunActiveInfoVo.setLikeNum(CommonUtil.enlargePraiseNum(lsShowYearService.getPraiseTotal()));
        //sunActiveInfoVo.setActiveRunning(lsShowYearService.checkDate());
        return sunActiveInfoVo;
    }*/

    /**
     * 获取广告
     * @param ads 广告
     * @author shenjun
     * @date 2021/2/23 15:37
     * @return HeadPicIsadVO
     */
    private HeadPicIsadVO getHeadPicIsadVO(List<Ad> ads){
        Ad ad = null;
        if(CollectionUtils.isNotEmpty(ads)){
            ad = ads.get(0);
        }
        HeadPicIsadVO result = new HeadPicIsadVO();
        if(ad !=null){
            result = HeadPicIsadWrapper.build().entityVO(ad);
            if(StringUtils.isNotBlank(result.getPicPath())){
                result.setPicPath(HttpUtil.getNetResourcePath(request,result.getPicPath()));
            }
        }
        return result;
    }

    /**
     * 获取活动规则1
     *
     * @param
     * @return ActiveRuleVO
     * @author jxc
     * @date 2021/1/21 15:41
     */
//    @Override
//    public ActiveRuleVO getActiveRule(Integer type) {
//        LsConfig lsConfig = new LsConfig();
//        lsConfig.setType(ConfigEnum.RULES.getCode());
//        switch (type){
//            case 1:
//                lsConfig.setKeyId(String.valueOf(ActiveRuleEnum.YEAR_PRAISE_RULE.getValue()));
//                break;
//        }
//        lsConfig.setDeleted(DeletedEnum.NOT_DELETE.getValue());
//        QueryWrapper queryWrapper = new QueryWrapper(lsConfig);
//        LsConfig config = configService.getOne(queryWrapper);
//        if(config == null){
//            return null;
//        }
//
//        ActiveRuleVO activeRuleVo = new ActiveRuleVO();
//        activeRuleVo.setRule(config.getKeyValue());
//        return activeRuleVo;
//    }

    /**
     * @description: 获取活动规则
     * @param type
     * @return: java.util.List<ActiveRuleVO>
     * @author: luzhichao
     * @date: 2021/1/29 10:09
     */
    @Override
    public JsonResult<List<ActiveRuleInfoVO>> getActiveRuleV2(Integer type) {
        //if (type >= ACTIVE_RULE[0] && type <= ACTIVE_RULE[1] ) {
            List<Ad> list = this.getLsAdListByType(type);
            // 排序后输出
            List<ActiveRuleInfoVO> result = list.stream().sorted(Comparator.comparing(Ad::getSort)).map(l -> {
                ActiveRuleInfoVO activeRuleInfoVo = new ActiveRuleInfoVO();
                activeRuleInfoVo.setContentUrl(l.getPicPath());
                return activeRuleInfoVo;
            }).collect(Collectors.toList());
            return JsonResult.ok(result);
       /* }
        return JsonResult.fail("参数异常");*/
    }

    /**
     * @description: 根据类型获取广告
     * @param type
     * @return: JsonResult<java.util.List<LsAdVO>>
     * @author: luzhichao
     * @date: 2021/1/29 16:37
     */
    @Override
    public JsonResult<List<LsAdVO>> getLsAd(Integer type) {
        if(type == null){
           return JsonResult.fail("未传参数！");
        }
        Config config=configService.getConfigByTypeAndKey(ConfigEnum.AD_TYPE_LABEL.getCode(),type.toString());
        //AdTypeEnum adTypeEnum = AdTypeEnum.fromValue(type);
        if (config == null) {
          return   JsonResult.fail("类型不存在！");
        }
        List<Ad> list = this.getLsAdListByType(type);
        // 排序后输出
        List<LsAdVO> result = list.stream().sorted(Comparator.comparing(Ad::getSort)).map(l -> {
            LsAdVO lsAdVo = new LsAdVO();
            BeanUtils.copyProperties(l, lsAdVo);
            return lsAdVo;
        }).collect(Collectors.toList());
        return JsonResult.ok(result);
    }


    /**
     * 摇一摇首页广告
     *
     * @return LsAdVO
     * @author qinmingtong
     * @date 2021/1/27 14:58
     */
    /*@Override
    public LsAdVO lsAdOfYaoYiYao() {
        LambdaQueryWrapper<Ad> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Ad::getType, 13);
        queryWrapper.orderByDesc(Ad::getId);
        queryWrapper.last("limit 1");
        Ad ad = baseMapper.selectOne(queryWrapper);
        LsAdVO lsAdVo = new LsAdVO();
        Optional.ofNullable(ad).ifPresent(e-> {
            BeanUtils.copyProperties(e,lsAdVo);
            lsAdVo.setPicPath(HttpUtil.getNetResourcePath(request, e.getPicPath()));
        });
        return lsAdVo;
    }*/


    /**
     * @param type 配置值key
     * @return java.util.List<LsAd>
     * @author zhangyongfeng
     * @date 2021/1/27 16:10
     */
    @Override
    public List<Ad> getLsAdListByType(int type) {
        List<Ad> list = new ArrayList<>();
        Object object = redisUtil.get(Configs.REDIS_LS_AD_TYPE + type);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        if (object != null) {

            list = gson.fromJson(object.toString(), new TypeToken<List<Ad>>() {
            }.getType());
        } else {
            list = baseMapper.getLsAdListByType(type);
            list.forEach(item -> {
                if (!StringUtils.isEmpty(item.getPicPath())) {
                    item.setPicPath(HttpUtil.getNetResourcePath(request, item.getPicPath()));
                }
            });
            redisUtil.set(Configs.REDIS_LS_AD_TYPE + type, gson.toJson(list));
        }
        return list;
    }

}
