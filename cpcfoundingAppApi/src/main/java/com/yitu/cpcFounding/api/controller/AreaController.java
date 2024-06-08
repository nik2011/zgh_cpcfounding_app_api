package com.yitu.cpcFounding.api.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yitu.cpcFounding.api.domain.Config;
import com.yitu.cpcFounding.api.service.ConfigService;
import com.yitu.cpcFounding.api.enums.ConfigEnum;
import com.yitu.cpcFounding.api.vo.AreaListVO;
import com.yitu.cpcFounding.api.vo.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pangshihe
 * @date 2021/1/21
 */
@Api(value = "区域", tags = "区域")
@RequestMapping("area")
@RestController
//@LoginRequired
public class AreaController {
    @Resource
    ConfigService configService;

    /**
     * 获取区域列表
     *
     * @return JsonResult<java.util.List < AreaListVO>>
     * @author pangshihe
     * @date 2021/1/21 11:34
     */
    @ApiOperation(value = "获取区域列表", notes = "获取区域列表")
    @ApiOperationSupport(author = "pangshihe")
    @PostMapping("getAreaList")
    public JsonResult<List<AreaListVO>> getAreaList() {
        List<Config> configs = configService.getConfigListByType(ConfigEnum.AREA.getCode());
        List<AreaListVO> areaListVOList = new ArrayList<>();
        configs.stream().forEach(it->{
            AreaListVO areaListVO=new AreaListVO();
            BeanUtils.copyProperties(it, areaListVO);
            areaListVOList.add(areaListVO);
        });
        return JsonResult.ok(areaListVOList);
    }
}
