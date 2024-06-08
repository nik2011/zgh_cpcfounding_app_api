package com.yitu.cpcFounding.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yitu.cpcFounding.api.domain.UserPrize;
import com.yitu.cpcFounding.api.vo.userprize.UserPrizeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户奖品表Mapper
 *
 * @author shenjun
 * @date 2021-01-21 11:24:36
 */
@Mapper
public interface UserPrizeMapper extends BaseMapper<UserPrize> {

    /**
     * @param prizeId, userId
     * @return int
     * @Description: 获取用户中奖数量
     * @author liuzhaowei
     * @date 2021/1/22
     */
    int getUserPrizeCountByPrizeId(@Param("prizeId") long prizeId, @Param("userId") long userId);

    int batchInsert(List<UserPrize> list);

    /**
     * 查询所有活动类型的用户中奖记录
     *
     * @param activeType 活动类型
     * @param count 需要查询的记录数
     * @return java.util.List<UserPrizeVO>
     * @author qinmingtong
     * @date 2021/1/22 16:08
     */
    List<UserPrizeVO> userPrizeList(@Param("activeType") Integer activeType, @Param("count") Integer count);

    /**
     * @param userId
     * @return int
     * @Description: 获取用户中奖数量
     * @author liuzhaowei
     * @date 2021/1/22
     */
    int getTodayUserPrizeCountByPrizeId(@Param("userId") long userId);

    /**
    * @Description: 获取用户中奖记录
    * @param userId
    * @author liuzhaowei
    * @date 2021/1/26
    * @return java.util.List<java.lang.Long>
     */
    List<Long>getUserPrizeCount(@Param("userId") long userId);

    /**
     * 中奖列表分页查询
     *
     * @param page 分页条件
     * @param activeType 活动类型
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.yitu.cpcFounding.api.vo.userprize.UserPrizeVO>
     * @author qinmingtong
     * @date 2021/4/27 15:09
     */
    Page<UserPrizeVO> userPrizePageList(Page<UserPrizeVO> page,@Param("activeType") Integer activeType);
}
