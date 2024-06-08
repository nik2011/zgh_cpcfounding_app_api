package com.yitu.cpcFounding.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yitu.cpcFounding.api.domain.PrizeExchangeCode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 奖品兑换码Mapper
 *
 * @author shenjun
 * @date 2021-01-21 11:02:18
 */
@Mapper
public interface PrizeExchangeCodeMapper extends BaseMapper<PrizeExchangeCode> {

    /**
     * @param prizeId
     * @return LsPrizeExchangeCode
     * @Description: 获取一个有效的兑换码
     * @author liuzhaowei
     * @date 2021/1/22
     */
    PrizeExchangeCode getActiveExchangeCode(@Param("prizeId") long prizeId);

    /**
     * @param id
     * @return int
     * @Description: 修改兑换码状态
     * @author liuzhaowei
     * @date 2021/1/22
     */
    int updateExchangeCodeStatus(@Param("id") long id);

}
