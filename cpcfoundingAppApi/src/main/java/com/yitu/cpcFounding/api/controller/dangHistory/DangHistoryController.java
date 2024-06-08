package com.yitu.cpcFounding.api.controller.dangHistory;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yitu.cpcFounding.api.auth.LoginRequired;
import com.yitu.cpcFounding.api.service.DangHistoryService;
import com.yitu.cpcFounding.api.utils.DateTimeUtil;
import com.yitu.cpcFounding.api.vo.JsonResult;
import com.yitu.cpcFounding.api.vo.dangHistory.DangHistoryDetailVO;
import com.yitu.cpcFounding.api.vo.dangHistory.DangHistoryLabelVO;
import com.yitu.cpcFounding.api.vo.dangHistory.DangHistoryListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * Project Name: zgh_cpcfounding_app_api
 * File Name: DangHistoryController
 * author: wangping
 * Date: 2021/6/7 15:56
 */
@Api(value = "党史模块", tags = "党史模块")
@RestController
@RequestMapping("/api/dangHis")
@Slf4j
public class DangHistoryController {

    @Autowired
    DangHistoryService dangHistoryService;

    /**
     * 获取党史列表数据（分页）
     * @param title 标题
     * @param pageIndex 页索引
     * @param pageSize 页大小
     * @author wangping
     * @date 2021-04-09
     * @return 获取党史列表
     */
    @PostMapping("/getDangHisList")
    @ApiOperation(value = "党史列表（分页）")
    @ApiOperationSupport(author = "王平")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageIndex", value = "页码", dataType = "int", required = true),
            @ApiImplicitParam(name = "pageSize", value = "页大小", dataType = "int", required = true),
            @ApiImplicitParam(name = "lableId", value = "标签分类 0为推荐,默认查询推荐", dataType = "String"),
            @ApiImplicitParam(name = "title", value = "党史名称", dataType = "String")
    })
    public JsonResult<IPage<DangHistoryListVO>> getDangHisList(@RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                                                               @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                               @RequestParam(value = "lableId",defaultValue = "0",required = false) String lableId,
                                                               @RequestParam(value = "title",required = false) String title) {
        UUID uuid = UUID.randomUUID();
        log.error("党史列表（分页）-开始执行("+uuid+")"+ DateTimeUtil.formatCurrent(""));
        JsonResult<IPage<DangHistoryListVO>> result = dangHistoryService.getDangHisList(lableId,title,pageIndex, pageSize);
        log.error("党史列表（分页）-结束执行("+uuid+")"+DateTimeUtil.formatCurrent(""));
        return result;
    }

    /**
     *
     * 获取党史详情
     * @param id
     * @return com.yitu.cpcFounding.api.vo.JsonResult<DangHistoryDetailVO>
     * @author wangping
     * @date 2021/6/7 17:12
     */
    @ApiOperation(value = "获取党史信息详情", notes = "获取党史信息详情")
    @ApiOperationSupport(author = "王平")
    @ApiImplicitParam(name = "id", value = "党史id", dataType = "int", required = true)
    @PostMapping("/detail")
    public JsonResult<DangHistoryDetailVO> detail(@RequestParam("id") @NotNull(message = "党史id不能为空") Integer id){
        return dangHistoryService.getDangHisDetail(id);
    }

    /**
     *
     * 添加党史阅读量
     * @param id
     * @return com.yitu.cpcFounding.api.vo.JsonResult
     * @author wangping
     * @date 2021/6/7 17:29
     */
    @ApiOperation(value = "党史阅读量添加", notes = "党史阅读量添加")
    @ApiOperationSupport(author = "王平")
    @ApiImplicitParam(name = "id", value = "党史id", dataType = "int", required = true)
    @PostMapping("/addVisitNum")
    public JsonResult addVisitNum(@RequestParam("id") @NotNull(message = "党史id不能为空") Integer id){
        return dangHistoryService.addVisitNum(id);
    }

    /**
     *
     * 阅读文章加入积分
     * @param id
     * @return com.yitu.cpcFounding.api.vo.JsonResult
     * @author wangping
     * @date 2021/6/15 9:02
     */
    @ApiOperation(value = "阅读文章添加积分", notes = "阅读文章添加积分")
    @ApiOperationSupport(author = "王平")
    @PostMapping("/readAddScore")
    @ApiImplicitParam(name = "id", value = "当前文章id", dataType = "int", required = true)
    @LoginRequired
    public JsonResult readAddScore(@RequestParam("id") @NotNull(message = "当前文章id不能为空") Integer id){
        return dangHistoryService.readAddScore(id);
    }

    /**
     *
     * 获取党史标签分类
     * @param
     * @return com.yitu.cpcFounding.admin.api.vo.JsonResult
     * @author wangping
     * @date 2021/6/4 17:07
     */
    @PostMapping("/getDangHistoryLabel")
    @ApiOperation(value = "获取党史标签分类")
    @ApiOperationSupport(author = "王平")
    public JsonResult<List<DangHistoryLabelVO>> getDangHistoryLabel() {
        return dangHistoryService.getDangHistoryLabel();
    }


}
