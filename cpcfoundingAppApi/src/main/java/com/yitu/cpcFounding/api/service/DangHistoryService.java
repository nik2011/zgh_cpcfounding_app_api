package com.yitu.cpcFounding.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.dangHistory.DangHistoryDetailVO;
import com.yitu.cpcFounding.api.vo.dangHistory.DangHistoryLabelVO;
import com.yitu.cpcFounding.api.vo.dangHistory.DangHistoryListVO;

import java.util.List;

/**
 * 党史小程序服务
 * File Name: DangHistoryService
 * author: wangping
 * Date: 2021/6/7 15:57
 */
public interface DangHistoryService {

    /**
     *
     * 党史列表查询
     * @param lableId 标签分类 默认0查询推荐
     * @param title 标题
     * @param pageIndex 页码
     * @param pageSize  页大小
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.dangHistory.DangHistoryListVO>
     * @author wangping
     * @date 2021/6/7 16:14
     */
    JsonResult<IPage<DangHistoryListVO>> getDangHisList(String lableId, String title, int pageIndex, int pageSize);


    /**
     *
     * 获取党史详情信息
     * @param id
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.dangHistory.DangHistoryDetailVO>
     * @author wangping
     * @date 2021/6/7 16:43
     */
    JsonResult<DangHistoryDetailVO> getDangHisDetail(Integer id);

    /**
     *
     * 添加党史阅读量
     * @param id
     * @return com.yitu.cpcFounding.api.vo.JsonResult
     * @author wangping
     * @date 2021/6/7 17:28
     */
    JsonResult addVisitNum(Integer id);

    /**
     *
     * 阅读文章添加积分
     * @param id 当前文章id
     * @return com.yitu.cpcFounding.api.vo.JsonResult
     * @author wangping
     * @date 2021/6/8 9:25
     */
    JsonResult readAddScore(Integer id);

    /**
     *
     * 获取党史标签
     * @return com.yitu.cpcFounding.admin.api.vo.JsonResult
     * @author wangping
     * @date 2021/6/7 11:00
     */
    JsonResult<List<DangHistoryLabelVO>> getDangHistoryLabel();


}
