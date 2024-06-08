package com.yitu.cpcFounding.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitu.cpcFounding.api.domain.PrizeBook;
import com.yitu.cpcFounding.api.vo.redis.YaoPrizeBookVo;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 奖品库表Mapper
 *
 * @author shenjun
 * @date 2021-01-21 11:02:18
 */
@Mapper
public interface PrizeBookMapper extends BaseMapper<PrizeBook> {

    /**
     * @param probabiliyValue 摇中的概率值
     * @return YaoPrizeBookVo
     * @Description: 根据概率获取奖品
     * @author liuzhaowei
     * @date 2021/1/21
     */
    YaoPrizeBookVo getPrizeByProbability(@Param("probabiliyValue") long probabiliyValue);

    /**
     * @param id
     * @return YaoPrizeBookVo
     * @Description: 奖品库存减一
     * @author liuzhaowei
     * @date 2021/1/21
     */
    int minusPrizeStock(@Param("id") long id);

    /**
     * @param
     * @return java.util.List<YaoPrizeBookVo>
     * @Description: 获取摇一摇奖品列表
     * @author liuzhaowei
     * @date 2021/1/22
     */
    List<YaoPrizeBookVo> getYaoPrizeList();

    /**
     * @param id    奖品id
     * @param count 库存减少个数
     * @description: minusPrizeStockCount
     * @return: int
     * @author: luzhichao
     * @date: 2021/1/22 14:34
     */
    int minusPrizeStockCount(@Param("id") long id, @Param("count") long count);

    /**
    * @Description:获取奖品未中奖概率奖品
    * @param
    * @author liuzhaowei
    * @date 2021/1/23
    * @return LsPrizeBook
     */
    PrizeBook getNoWinningProbabilityPrize();

}
