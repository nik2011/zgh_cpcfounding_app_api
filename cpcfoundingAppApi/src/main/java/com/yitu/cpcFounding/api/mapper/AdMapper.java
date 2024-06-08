package com.yitu.cpcFounding.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitu.cpcFounding.api.domain.Ad;
import com.yitu.cpcFounding.api.vo.ad.WXCodeVO;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 广告表Mapper
 *
 * @author shenjun
 * @date 2021-01-21 11:02:18
 */
@Mapper
public interface AdMapper extends BaseMapper<Ad> {
    /**
     * 获取首页广告
     *
     * @param
     * @return java.util.List<LsAd>
     * @author jxc
     * @date 2021/1/21 12:14
     */
    List<Ad> getIndexAdInfo(@Param("list") List<Integer> list);

    /**
     * 获取小程序码信息
     *
     * @param type
     * @return_type List<WXCodeVO>
     * @author clj
     * @date 2021-01-25 07:26:31
     */
    List<WXCodeVO> getWXCodeInfo(@Param("type") Integer type);


    /**
     * 根据广告类型取出广告列表
     *
     * @param type
     * @return java.util.List<LsAd>
     * @author zhangyongfeng
     * @date 2021/1/27 16:00
     */
    List<Ad> getLsAdListByType(@Param("type") Integer type);

}
