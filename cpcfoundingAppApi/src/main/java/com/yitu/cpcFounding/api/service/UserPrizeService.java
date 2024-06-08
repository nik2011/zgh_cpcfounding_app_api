package com.yitu.cpcFounding.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yitu.cpcFounding.api.dto.userprize.UserPrizeDetailDTO;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.userprize.UserPrizeDetailVO;
import com.yitu.cpcFounding.api.vo.userprize.UserPrizeListVO;
import com.yitu.cpcFounding.api.vo.userprize.UserPrizeVO;

import java.util.List;

/**
 * 用户奖品表服务
 *
 * @author shenjun
 * @date 2021-01-21 11:47:39
 */
public interface UserPrizeService {

    /**
     * 用户奖品列表
     *
     * @return JsonResult<java.util.List < UserPrizeVO>>
     * @author chenpinjia
     * @date 2021/1/21 15:04
     */
    JsonResult<List<UserPrizeListVO>> getUserPrizeList();


    /**
     * 用户奖品明细
     *
     * @param userPrizeId 用户奖品id
     * @return JsonResult<UserPrizeDetailVO>
     * @author chenpinjia
     * @date 2021/1/21 16:52
     */
    JsonResult<UserPrizeDetailVO> getUserPrizeDetail(Long userPrizeId);


    /**
     * 保存用户领奖信息
     *
     * @param saveInfo 保存的明细
     * @return JsonResult
     * @author chenpinjia
     * @date 2021/1/21 17:42
     */
    JsonResult saveUserPrizeDetailInfo(UserPrizeDetailDTO saveInfo);

    /**
     * @param prizeId, userId, userName
     * @return int
     * @Description: 添加记录
     * @author liuzhaowei
     * @date 2021/1/21
     */
    long insertRecord(long prizeId, long userId, String userName, String exchangeCode, String ip);

    /**
     * 摇一摇首页中奖列表接口
     *
     * @return JsonResult<java.util.List < UserPrizeVO>>
     * @author qinmingtong
     * @date 2021/1/21 18:19
     */
    JsonResult<List<UserPrizeVO>> userPrizeOfYaoYiYao();

    /**
     * 查询所有活动类型的用户中奖记录
     *
     * @param activeType 活动类型
     * @param count      需要查询的记录数
     * @return JsonResult<java.util.List < UserPrizeVO>>
     * @author qinmingtong
     * @date 2021/1/22 16:07
     */
    JsonResult<List<UserPrizeVO>> userPrizeList(Integer activeType, Integer count);

    /**
     * 中奖列表分页查询接口
     *
     * @param pageIndex  当前页
     * @param pageSize   每页大小
     * @param activeType 活动类型
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.yitu.mayday.api.vo.userprize.UserPrizeVO>
     * @author qinmingtong
     * @date 2021/4/27 15:14
     */
    Page<UserPrizeVO> userPrizePageList(Integer pageIndex, Integer pageSize, Integer activeType);
}
